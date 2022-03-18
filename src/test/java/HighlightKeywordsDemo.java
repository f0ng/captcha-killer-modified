import java.awt.Color;

import java.util.HashSet;
import java.util.Set;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class HighlightKeywordsDemo {

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        JTextPane editor = new JTextPane();
        editor.getDocument().addDocumentListener(new SyntaxHighlighter(editor));
        frame.getContentPane().add(editor);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setVisible(true);
    }

}


/**
 * 当文本输入区的有字符插入或者删除时, 进行高亮.
 * <p>
 * 要进行语法高亮, 文本输入组件的document要是styled document才行. 所以不要用JTextArea. 可以使用JTextPane.
 *
 * @author Biao
 */

class SyntaxHighlighter implements DocumentListener {
    private Set<String> keywords;
    private Style keywordStyle;
    private Style normalStyle;

    public SyntaxHighlighter(JTextPane editor) {
        // 准备着色使用的样式
        keywordStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        normalStyle = ((StyledDocument) editor.getDocument()).addStyle("Keyword_Style", null);
        StyleConstants.setForeground(keywordStyle, Color.RED);
        StyleConstants.setForeground(normalStyle, Color.BLACK);

        // 准备关键字
        keywords = new HashSet<String>();
        keywords.add("public");
        keywords.add("protected");
        keywords.add("private");
        keywords.add("_int9");
        keywords.add("float");
        keywords.add("double");
    }


    public void colouring(StyledDocument doc, int pos, int len) throws BadLocationException {
        // 取得插入或者删除后影响到的单词.
        // 例如"public"在b后插入一个空格, 就变成了:"pub lic", 这时就有两个单词要处理:"pub"和"lic"
        // 这时要取得的范围是pub中p前面的位置和lic中c后面的位置
        int start = indexOfWordStart(doc, pos);
        int end = indexOfWordEnd(doc, pos + len);

        char ch;

        while (start < end) {

            ch = getCharAt(doc, start);

            if (Character.isLetter(ch) || ch == '_') {

                // 如果是以字母或者下划线开头, 说明是单词

                // pos为处理后的最后一个下标

                start = colouringWord(doc, start);

            } else {
                SwingUtilities.invokeLater(new ColouringTask(doc, start, 1, normalStyle));
                ++start;
            }

        }

    }


    /**
     * 对单词进行着色, 并返回单词结束的下标.
     *
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */

    public int colouringWord(StyledDocument doc, int pos) throws BadLocationException {

        int wordEnd = indexOfWordEnd(doc, pos);

        String word = doc.getText(pos, wordEnd - pos);


        if (keywords.contains(word)) {

            // 如果是关键字, 就进行关键字的着色, 否则使用普通的着色.

            // 这里有一点要注意, 在insertUpdate和removeUpdate的方法调用的过程中, 不能修改doc的属性.

            // 但我们又要达到能够修改doc的属性, 所以把此任务放到这个方法的外面去执行.

            // 实现这一目的, 可以使用新线程, 但放到swing的事件队列里去处理更轻便一点.

            SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, keywordStyle));

        } else {

            SwingUtilities.invokeLater(new ColouringTask(doc, pos, wordEnd - pos, normalStyle));

        }


        return wordEnd;

    }


    /**
     * 取得在文档中下标在pos处的字符.
     * <p>
     * <p>
     * <p>
     * 如果pos为doc.getLength(), 返回的是一个文档的结束符, 不会抛出异常. 如果pos<0, 则会抛出异常.
     * <p>
     * 所以pos的有效值是[0, doc.getLength()]
     *
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */

    public char getCharAt(Document doc, int pos) throws BadLocationException {
        return doc.getText(pos, 1).charAt(0);
    }


    /**
     * 取得下标为pos时, 它所在的单词开始的下标. Â±wor^dÂ± (^表示pos, Â±表示开始或结束的下标)
     *
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */

    public int indexOfWordStart(Document doc, int pos) throws BadLocationException {
        // 从pos开始向前找到第一个非单词字符.
        for (; pos > 0 && isWordCharacter(doc, pos - 1); --pos) ;
        return pos;
    }


    /**
     * 取得下标为pos时, 它所在的单词结束的下标. Â±wor^dÂ± (^表示pos, Â±表示开始或结束的下标)
     *
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */

    public int indexOfWordEnd(Document doc, int pos) throws BadLocationException {

        // 从pos开始向前找到第一个非单词字符.
        for (; isWordCharacter(doc, pos); ++pos) ;
        return pos;
    }


    /**
     * 如果一个字符是字母, 数字, 下划线, 则返回true.
     *
     * @param doc
     * @param pos
     * @return
     * @throws BadLocationException
     */

    public boolean isWordCharacter(Document doc, int pos) throws BadLocationException {
        char ch = getCharAt(doc, pos);
        if (Character.isLetter(ch) || Character.isDigit(ch) || ch == '_') {
            return true;
        }
        return false;
    }


    @Override
    public void changedUpdate(DocumentEvent e) {
    }


    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            colouring((StyledDocument) e.getDocument(), e.getOffset(), e.getLength());
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }
    }


    @Override
    public void removeUpdate(DocumentEvent e) {
        try {
            // 因为删除后光标紧接着影响的单词两边, 所以长度就不需要了
            colouring((StyledDocument) e.getDocument(), e.getOffset(), 0);
        } catch (BadLocationException e1) {
            e1.printStackTrace();
        }

    }


    /**
     * 完成着色任务
     *
     * @author Biao
     */
    private class ColouringTask implements Runnable {
        private StyledDocument doc;
        private Style style;
        private int pos;
        private int len;

        public ColouringTask(StyledDocument doc, int pos, int len, Style style) {
            this.doc = doc;
            this.pos = pos;
            this.len = len;
            this.style = style;
        }

        public void run() {
            try {
                // 这里就是对字符进行着色
                doc.setCharacterAttributes(pos, len, style, true);
            } catch (Exception e) {
            }
        }
    }

}