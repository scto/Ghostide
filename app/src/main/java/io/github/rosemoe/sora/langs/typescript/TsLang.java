package io.github.rosemoe.sora.langs.typescript;

import io.github.rosemoe.sora.interfaces.AutoCompleteProvider;
import io.github.rosemoe.sora.interfaces.NewlineHandler;
import io.github.rosemoe.sora.interfaces.CodeAnalyzer;
import io.github.rosemoe.sora.interfaces.EditorLanguage;
import io.github.rosemoe.sora.langs.internal.MyCharacter;
import io.github.rosemoe.sora.langs.java.JavaAutoComplete;
import io.github.rosemoe.sora.langs.java.JavaLanguage;
import io.github.rosemoe.sora.text.TextUtils;
import io.github.rosemoe.sora.widget.SymbolPairMatch;
import io.github.rosemoe.sora.widget.CodeEditor;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;

public class TsLang implements EditorLanguage {
  public static String[] list = {
    "abstract",
    "any",
    "array",
    "as",
    "async",
    "await",
    "break",
    "case",
    "catch",
    "class",
    "const",
    "constructor",
    "continue",
    "declare",
    "default",
    "delete",
    "do",
    "while",
    "else",
    "enum",
    "export",
    "extends",
    "false",
    "finally",
    "for",
    "of",
    "from",
    "function",
    "if",
    "else",
    "implements",
    "import",
    "in",
    "instanceof",
    "interface",
    "keyof",
    "let",
    "module",
    "namespace",
    "never",
    "null",
    "number",
    "object",
    "of",
    "private",
    "protected",
    "public",
    "readonly",
    "require",
    "return",
    "set",
    "static",
    "string",
    "super",
    "switch",
    "symbol",
    "template",
    "this",
    "throw",
    "true",
    "try",
    "catch",
    "tuple",
    "typeof",
    "undefined",
    "unique symbol",
    "unknown",
    "var",
    "void",
    "while",
    "with"
  };
  private final NewlineHandler[] newLineHandlers =
      new NewlineHandler[] {
        new BraceHandler(), new TwoIndentHandler(), new JavaDocStartHandler(), new JavaDocHandler()
      };
  protected CodeEditor editor;
  protected JavaLanguage lang;

  public TsLang(CodeEditor editor) {
    this.editor = editor;
  }

  @Override
  public CodeAnalyzer getAnalyzer() {
    return new TypeScriptCodeAnalyzer();
  }

  @Override
  public AutoCompleteProvider getAutoCompleteProvider() {
    var tsAuto = new TypeScriptAutoComplete();
    tsAuto.setKeywords(list);
    return tsAuto;
  }

  @Override
  public boolean isAutoCompleteChar(char ch) {
    return MyCharacter.isJavaIdentifierStart(ch) || ch == '"' || ch == '.' || ch == '<';
  }

  @Override
  public int getIndentAdvance(String content) {
    try {
      TypeScriptLexer lexer = new TypeScriptLexer(CharStreams.fromString(content));
      Token token;
      int to = 0;
      while (((token = lexer.nextToken()) != null && token.getType() != token.EOF)) {
        switch (token.getType()) {
          case TypeScriptLexer.OpenBrace:
            to++;
            break;
          case TypeScriptLexer.CloseBrace:
            to--;
            break;
        }
      }
      to = Math.max(0, to);
      return to * 2;
    } catch (Exception err) {
      err.printStackTrace();
    }
    return 0;
  }

  @Override
  public boolean useTab() {
    return true;
  }

  @Override
  public CharSequence format(CharSequence text) {
    return javaFormat(text.toString());
  }

  @Override
  public SymbolPairMatch getSymbolPairs() {
    return new SymbolPairMatch.DefaultSymbolPairs();
  }

  @Override
  public NewlineHandler[] getNewlineHandlers() {
    return newLineHandlers;
  }

  private void sum(StringBuilder content, int indents) {
    for (int i = 0; i < indents; i++) {
      content.append('\t');
    }
  }

  private String formattingProcess(final String s) {
    final StringBuilder sb = new StringBuilder(4096);
    final char[] charArray = s.toCharArray();
    final int length = charArray.length;
    int i = 0;
    int n = 0;
    int n2 = 0;
    int n3 = 0;
    int n4 = 0;
    int n5 = 0;
    int n6 = 0;
    while (i < length) {
      final char c = charArray[i];
      int n10 = 0;
      int n11 = 0;
      Label_0094:
      {
        Label_0082:
        {
          // n = 1 if it's a one line comment
          if (n != 0) {
            if (c == '\n') {
              sb.append(c);
              sum(sb, n4);
              n = 0;
            } else {
              sb.append(c);
            }
          } // check end of multiple lines comment
          else if (n2 != 0) {
            if (c == '*') {
              final int n7 = i + 1;
              final char c2 = charArray[n7];
              if (c2 == '/') {
                sb.append(c);
                sb.append(c2);
                n2 = 0;
                i = n7;
                break Label_0082;
              }
            }
            sb.append(c);
            // commit
            if (c == '\n') sum(sb, n4);
          } else if (n3 != 0) {
            sb.append(c);
            n3 = 0;
          } else if (c == '\\') {
            sb.append(c);
            n3 = 1;
          } else if (n5 != 0) {
            if (c == '\'') {
              sb.append(c);
              n5 = 0;
            } else {
              sb.append(c);
            }
          } else if (n6 != 0) {
            if (c == '\"') {
              sb.append(c);
              n6 = 0;
            } else {
              sb.append(c);
            }
          } else {
            // check start of comments
            if (c == '/') {
              final int n8 = i + 1;
              final char c3 = charArray[n8];
              // start of one line comment
              if (c3 == '/') {
                sb.append(c);
                sb.append(c3);
                n = 1;
                i = n8;
                break Label_0082;
              }
              // start of multiple lines comment
              if (c3 == '*') {
                sb.append(c);
                sb.append(c3);
                n2 = 1;
                i = n8;
                break Label_0082;
              }
            }
            if (c != '\n') {
              if (c == '\'') {
                n5 = 1;
              }
              int n9;
              if (c == '\"') {
                n9 = 1;
              } else {
                n9 = n6;
              }
              if (c == '{') {
                ++n4;
              }
              n10 = n4;
              if (c == '}') {
                n10 = --n4;
                if (sb.charAt(sb.length() - 1) == '\t') {
                  sb.deleteCharAt(sb.length() - 1);
                  n10 = n4;
                }
              }
              sb.append(c);
              n11 = n9;
              break Label_0094;
            }
            sb.append(c);
            sum(sb, n4);
          }
        }
        final int n12 = n6;
        n10 = n4;
        n11 = n12;
      }
      ++i;
      final int n13 = n10;
      n6 = n11;
      n4 = n13;
    }
    return sb.toString();
  }

  public String javaFormat(String text) {
    return formattingProcess(
        Arrays.stream(text.split("\n")).map(s -> s.trim()).collect(Collectors.joining("\n")));
  }

  class BraceHandler implements NewlineHandler {

    @Override
    public boolean matchesRequirement(String beforeText, String afterText) {
      return beforeText.endsWith("{") && afterText.startsWith("}");
    }

    @Override
    public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceBefore = getIndentAdvance(beforeText);
      int advanceAfter = getIndentAdvance(afterText);
      String text;
      StringBuilder sb =
          new StringBuilder("\n")
              .append(TextUtils.createIndent(count + advanceBefore, tabSize, useTab()))
              .append('\n')
              .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
      int shiftLeft = text.length() + 1;
      return new HandleResult(sb, shiftLeft);
    }
  }

  class JavaDocStartHandler implements NewlineHandler {

    private boolean shouldCreateEnd = true;

    @Override
    public boolean matchesRequirement(String beforeText, String afterText) {
      return beforeText.trim().startsWith("/**");
    }

    @Override
    public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceAfter = getIndentAdvance(afterText);
      String text = "";
      StringBuilder sb =
          new StringBuilder()
              .append("\n")
              .append(TextUtils.createIndent(count + advanceAfter, tabSize, useTab()))
              .append(" * ");
      if (shouldCreateEnd) {
        sb.append("\n")
            .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()))
            .append(" */");
      }
      return new HandleResult(sb, text.length() + 4);
    }
  }

  class JavaDocHandler implements NewlineHandler {

    @Override
    public boolean matchesRequirement(String beforeText, String afterText) {
      return beforeText.trim().startsWith("*") && !beforeText.trim().startsWith("*/");
    }

    @Override
    public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceAfter = getIndentAdvance(afterText);
      StringBuilder sb =
          new StringBuilder()
              .append("\n")
              .append(TextUtils.createIndent(count + advanceAfter, tabSize, useTab()))
              .append("* ");
      return new HandleResult(sb, 0);
    }
  }

  class TwoIndentHandler implements NewlineHandler {

    @Override
    public boolean matchesRequirement(String beforeText, String afterText) {
      //  Log.d("BeforeText", beforeText);
      if (beforeText.replace("\r", "").trim().startsWith(".")) {
        return false;
      }
      return beforeText.endsWith(")") && !afterText.startsWith(";");
    }

    @Override
    public HandleResult handleNewline(String beforeText, String afterText, int tabSize) {
      int count = TextUtils.countLeadingSpaceCount(beforeText, tabSize);
      int advanceAfter = getIndentAdvance(afterText) + (4 * 2);
      String text;
      StringBuilder sb =
          new StringBuilder()
              .append('\n')
              .append(text = TextUtils.createIndent(count + advanceAfter, tabSize, useTab()));
      int shiftLeft = 0;
      return new HandleResult(sb, shiftLeft);
    }
  }
}
