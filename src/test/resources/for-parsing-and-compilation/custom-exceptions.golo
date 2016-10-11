module DeclaringException

exception SomethingIsWrong

exception Ooops = { a, b, c }

exception WithMessage = { foo, message }

exception WithCause = { foo, cause }

exception WithMessageAndCause = { foo, message, cause }
