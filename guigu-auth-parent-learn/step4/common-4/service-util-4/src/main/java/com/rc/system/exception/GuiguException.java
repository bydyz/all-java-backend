package com.rc.system.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data       // 编译时生成 get set
@AllArgsConstructor     // 编译时生成 有参构造
@NoArgsConstructor      // 编译时生成 无参构造
public class GuiguException extends RuntimeException{

    private Integer code;
    private String msg;

}
