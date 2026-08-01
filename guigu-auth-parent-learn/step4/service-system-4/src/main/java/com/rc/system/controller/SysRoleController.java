package com.rc.system.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rc.common.result.Result;
import com.rc.model.system.SysRole;
import com.rc.model.vo.SysRoleQueryVo;
import com.rc.system.exception.GuiguException;
import com.rc.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 此处是集成了 swagger 且 多加上了 结果统一格式 的代码

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;


    //3 条件分页查询      看来像这种比较麻烦的是没有被MP封装，需要从 controller 到 service 到 mapper 到 XML文件 都要自己写
    // page当前页  limit每页记录数
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")       // 此处最终请求的url是啥？似乎可以多传参数，且似乎多传的就在sysRoleQueryVo里面了，为啥要有VO？？？
    public Result findPageQueryRole(@PathVariable Long page,
                                    @PathVariable Long limit,
                                    SysRoleQueryVo sysRoleQueryVo) {
        //创建page对象
        Page<SysRole> pageParam = new Page<>(page, limit);
        //调用service方法
        IPage<SysRole> pageModel = sysRoleService.selectPage(pageParam, sysRoleQueryVo);
        //返回
        return Result.ok(pageModel);
    }



    // @ApiOperation("逻辑删除接口")
    // //2 逻辑删除接口      添加完接口后似乎要重启服务！      在浏览器中通过url访问是get请求，因此本url在浏览器中显示的是405错误
    // @DeleteMapping("remove/{id}")
    // public boolean removeRole(@PathVariable Long id) {      // @PathVariable Long id  表示获取路径中的id，类型要对应
    //     //调用方法删除
    //     boolean isSuccess = sysRoleService.removeById(id);
    //     return isSuccess;
    // }

    @ApiOperation("逻辑删除接口")
    //2 逻辑删除接口      添加完接口后似乎要重启服务！      在浏览器中通过url访问是get请求，因此本url在浏览器中显示的是405错误
    @DeleteMapping("remove/{id}")
    public Result removeRole(@PathVariable Long id) {      // @PathVariable Long id  表示获取路径中的id，类型要对应
        //调用方法删除
        boolean isSuccess = sysRoleService.removeById(id);
        if(isSuccess) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }



    // @ApiOperation("查询所有记录")
    // // http://localhost:8800/admin/system/sysRole/findAll        浏览器上看到的是所有的json数据，但是日期格式有问题
    // //1 查询所有记录
    // @GetMapping("findAll")
    // public List<SysRole> findAllRole() {
    //     //调用service
    //     List<SysRole> list = sysRoleService.list();
    //     return list;
    // }

    @ApiOperation("查询所有记录")
    // http://localhost:8800/admin/system/sysRole/findAll        浏览器上看到的是所有的json数据，但是日期格式有问题
    //1 查询所有记录
    @GetMapping("findAll")
    public Result findAllRole() {


        // 倘若没有其他任何处理，调用此接口会出现异常，接口表现是，报500，且返回的结构不是我们要统一的result结构
        // int i = 9/0;

        // 异常处理的三种方式：
        //     1.全局异常处理        只要出现异常，均会执行此处理
        //     2.特定异常处理        只有出现特定异常，才会执行此处理
        //     3.自定义异常处理       自己编写异常类，手动抛出异常



        //TODO 模拟异常效果  ArithmeticException
        try {
            int i = 9/0;
        }catch (Exception e) {
            //手动抛出异常   才可能进行 自定义异常处理
            throw new GuiguException(20001, "执行自定义异常处理");
        }



        //调用service
        List<SysRole> list = sysRoleService.list();
        return Result.ok(list);
    }
}

