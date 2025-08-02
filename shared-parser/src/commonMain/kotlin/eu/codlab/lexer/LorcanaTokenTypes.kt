package eu.codlab.lexer

import guru.zoroark.lixy.LixyTokenType

enum class LorcanaTokenTypes : LixyTokenType {
    Not,
    OpenParenthesis,
    CloseParenthesis,
    Comparator,
    Separator,
    RegularToken,
    DoubleQuote
}
