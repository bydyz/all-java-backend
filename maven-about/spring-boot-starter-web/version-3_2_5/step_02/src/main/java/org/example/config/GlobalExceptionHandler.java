package org.example.config;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * ① @RequestBody + @Valid 校验失败
     *    -> MethodArgumentNotValidException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleBody(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(fe ->
                errors.put(fe.getField(), fe.getDefaultMessage()));
        // 类级别的约束（如 @AssertTrue 在类上的情况）也一并收集
        ex.getBindingResult().getGlobalErrors().forEach(ge ->
                errors.put("_global", ge.getDefaultMessage()));
        return Map.of("code", 400, "message", "参数校验失败", "errors", errors);
    }

    /**
     * ② 方法参数（@PathVariable / @RequestParam）校验失败
     *    Spring 6.1+ 内置方法校验 -> HandlerMethodValidationException
     */
    @ExceptionHandler(HandlerMethodValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleMethodValidation(HandlerMethodValidationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getAllValidationResults().forEach(result ->
                result.getResolvableErrors().forEach(err ->
                        errors.put(result.getMethodParameter().getParameterName(),
                                err.getDefaultMessage())));
        return Map.of("code", 400, "message", "参数校验失败", "errors", errors);
    }

    /**
     * ③ 类上标了 @Validated 时走 AOP 校验 -> ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleConstraint(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(v ->
                errors.put(v.getPropertyPath().toString(), v.getMessage()));
        return Map.of("code", 400, "message", "参数校验失败", "errors", errors);
    }
}
