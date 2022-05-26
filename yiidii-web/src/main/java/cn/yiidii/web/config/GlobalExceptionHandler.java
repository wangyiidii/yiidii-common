package cn.yiidii.web.config;

import cn.hutool.core.util.StrUtil;
import cn.yiidii.web.R;
import cn.yiidii.web.exception.BaseUncheckedException;
import cn.yiidii.web.exception.BizException;
import cn.yiidii.web.exception.code.ExceptionCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 全局异常统一处理
 *
 * @author zuihou
 * @date 2017-12-13 17:04
 */
@Slf4j
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@RestControllerAdvice
public abstract class GlobalExceptionHandler {

    @PostConstruct
    private void init() {
        log.info("Condition type is WebApplication, init GlobalExceptionHandler...");
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> bizException(BizException ex) {
        log.debug("BizException: {}", ex);
        log.warn("BizException: {}");
        return R.failed(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public R<?> noHandlerFoundException(NoHandlerFoundException ex) {
        log.debug("NoHandlerFoundException: {}", ex);
        log.warn("NoHandlerFoundException: {}");
        return R.failed(ExceptionCode.NOT_FOUND.getCode(), ExceptionCode.NOT_FOUND.getMsg());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> httpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.debug("HttpMessageNotReadableException: {}", ex);
        log.warn("HttpMessageNotReadableException: {}");
        String message = ex.getMessage();
        if (StrUtil.containsAny(message, "Could not read document:")) {
            String msg = String.format("无法正确的解析json类型的参数：%s", StrUtil.subBetween(message, "Could not read document:", " at "));
            return R.failed(ExceptionCode.PARAM_EX.getCode(), msg);
        }
        return R.failed(ExceptionCode.PARAM_EX.getCode(), ExceptionCode.PARAM_EX.getMsg());
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> bindException(BindException ex) {
        log.debug("BindException: {}", ex);
        log.warn("BindException: {}");
        try {
            String msg = Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage();
            if (StrUtil.isNotEmpty(msg)) {
                return R.failed(ExceptionCode.PARAM_EX.getCode());
            }
        } catch (Exception ee) {
            log.debug("获取异常描述失败", ee);
        }
        StringBuilder msg = new StringBuilder();
        List<FieldError> fieldErrors = ex.getFieldErrors();
        fieldErrors.forEach((oe) ->
                msg.append("参数:[").append(oe.getObjectName())
                        .append(".").append(oe.getField())
                        .append("]的传入值:[").append(oe.getRejectedValue()).append("]与预期的字段类型不匹配.")
        );
        return R.failed(ExceptionCode.PARAM_EX.getCode(), msg.toString());
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.debug("MethodArgumentTypeMismatchException: {}", ex);
        log.warn("MethodArgumentTypeMismatchException: {}");
        String msg = "参数：[" + ex.getName() + "]的传入值：[" + ex.getValue() +
                "]与预期的字段类型：[" + Objects.requireNonNull(ex.getRequiredType()).getName() + "]不匹配";
        return R.failed(ExceptionCode.PARAM_EX.getCode(), msg);
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> illegalStateException(IllegalStateException ex) {
        log.debug("IllegalStateException: {}", ex);
        log.warn("IllegalStateException: {}");
        return R.failed(ExceptionCode.ILLEGAL_ARGUMENT_EX.getCode(), ExceptionCode.ILLEGAL_ARGUMENT_EX.getMsg());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> missingServletRequestParameterException(MissingServletRequestParameterException ex) {
        log.debug("MissingServletRequestParameterException: {}", ex);
        log.warn("MissingServletRequestParameterException: {}");
        return R.failed(ExceptionCode.ILLEGAL_ARGUMENT_EX.getCode(), "缺少必须的[" + ex.getParameterType() + "]类型的参数[" + ex.getParameterName() + "]");
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> nullPointerException(NullPointerException ex) {
        log.debug("NullPointerException: {}", ex);
        log.warn("NullPointerException: {}");
        return R.failed(ExceptionCode.NULL_POINT_EX.getCode(), ExceptionCode.NULL_POINT_EX.getMsg());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> illegalArgumentException(IllegalArgumentException ex) {
        log.debug("IllegalArgumentException: {}", ex);
        log.warn("IllegalArgumentException: {}");
        return R.failed(ExceptionCode.ILLEGAL_ARGUMENT_EX.getCode(), ExceptionCode.ILLEGAL_ARGUMENT_EX.getMsg());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> httpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        log.debug("HttpMediaTypeNotSupportedException: {}", ex);
        log.warn("HttpMediaTypeNotSupportedException: {}");
        MediaType contentType = ex.getContentType();
        if (contentType != null) {
            return R.failed(ExceptionCode.MEDIA_TYPE_EX.getCode(), "请求类型(Content-Type)[" + contentType.toString() + "] 与实际接口的请求类型不匹配");
        }
        return R.failed(ExceptionCode.MEDIA_TYPE_EX.getCode(), "无效的Content-Type类型");
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> missingServletRequestPartException(MissingServletRequestPartException ex) {
        log.debug("MissingServletRequestPartException: {}", ex);
        log.warn("MissingServletRequestPartException: {}");
        return R.failed(ExceptionCode.METHOD_NOT_ALLOWED.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getMsg());
    }

    @ExceptionHandler(ServletException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> servletException(ServletException ex) {
        log.debug("ServletException: {}", ex);
        log.warn("ServletException: {}");
        String msg = "UT010016: Not a multi part request";
        if (msg.equalsIgnoreCase(ex.getMessage())) {
            return R.failed(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getMsg());
        }
        return R.failed(ExceptionCode.SYSTEM_BUSY.getCode());
    }

    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> multipartException(MultipartException ex) {
        log.debug("MultipartException: {}", ex);
        log.warn("MultipartException: {}");
        return R.failed(ExceptionCode.REQUIRED_FILE_PARAM_EX.getCode(), ExceptionCode.REQUIRED_FILE_PARAM_EX.getMsg());
    }

    /**
     * jsr 规范中的验证异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> constraintViolationException(ConstraintViolationException ex) {
        log.debug("ConstraintViolationException: {}", ex);
        log.warn("ConstraintViolationException: {}");
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        String message = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(";"));

        return R.failed(ExceptionCode.BASE_VALID_PARAM.getCode(), message);
    }

    /**
     * spring 封装的参数验证异常， 在controller中没有写result参数时，会进入
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.debug("MethodArgumentNotValidException: {}", ex);
        log.warn("MethodArgumentNotValidException: {}");
        return R.failed(ExceptionCode.BASE_VALID_PARAM.getCode(), Objects.requireNonNull(ex.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 其他异常
     *
     * @param ex 其他异常
     * @return
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> otherExceptionHandler(Throwable ex) {
        log.debug("Exception: {}", ex);
        log.warn("Exception: {}");
        if (ex.getCause() instanceof BizException) {
            return this.bizException((BizException) ex.getCause());
        }
        return R.failed(ExceptionCode.SYSTEM_BUSY.getCode(), ExceptionCode.SYSTEM_BUSY.getMsg());
    }

    @ExceptionHandler(BaseUncheckedException.class)
    @ResponseStatus(HttpStatus.OK)
    public R<?> baseUncheckedException(BaseUncheckedException ex) {
        log.debug("BaseUncheckedException: {}", ex);
        log.warn("BaseUncheckedException: {}");
        return R.failed(ex.getCode());
    }


    /**
     * 返回状态码:405
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public R<?> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        log.debug("HttpRequestMethodNotSupportedException: {}", ex);
        log.warn("HttpRequestMethodNotSupportedException: {}");
        return R.failed(ExceptionCode.METHOD_NOT_ALLOWED.getCode(), ExceptionCode.METHOD_NOT_ALLOWED.getMsg());
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R<?> sqlException(SQLException ex) {
        log.debug("SQLException: {}", ex);
        log.warn("SQLException: {}");
        return R.failed(ExceptionCode.SQL_EX.getCode(), ExceptionCode.SQL_EX.getMsg());
    }

}
