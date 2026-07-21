package com.rc.system.controller;

import com.rc.model.system.SysRole;
import com.rc.system.service.SysRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// 此处是不包含 swagger 的代码

// @RestController
// @RequestMapping("/admin/system/sysRole")
// public class SysRoleController {
//
//     @Autowired
//     private SysRoleService sysRoleService;
//
//
//     //2 逻辑删除接口      添加完接口后似乎要重启服务！      在浏览器中通过url访问是get请求，因此本url在浏览器中显示的是405错误
//     @DeleteMapping("remove/{id}")
//     public boolean removeRole(@PathVariable Long id) {      // @PathVariable Long id  表示获取路径中的id，类型要对应
//         //调用方法删除
//         boolean isSuccess = sysRoleService.removeById(id);
//         return isSuccess;
//     }
//
//
//
//     // http://localhost:8800/admin/system/sysRole/findAll        浏览器上看到的是所有的json数据，但是日期格式有问题
//     //1 查询所有记录
//     @GetMapping("findAll")
//     public List<SysRole> findAllRole() {
//         //调用service
//         List<SysRole> list = sysRoleService.list();
//         return list;
//     }
// }








// 此处是集成了 swagger 的代码       其实就是多了 @Api  和  @ApiOperation

@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;


    @ApiOperation("逻辑删除接口")
    //2 逻辑删除接口      添加完接口后似乎要重启服务！      在浏览器中通过url访问是get请求，因此本url在浏览器中显示的是405错误
    @DeleteMapping("remove/{id}")
    public boolean removeRole(@PathVariable Long id) {      // @PathVariable Long id  表示获取路径中的id，类型要对应
        //调用方法删除
        boolean isSuccess = sysRoleService.removeById(id);
        return isSuccess;
    }



    @ApiOperation("查询所有记录")
    // http://localhost:8800/admin/system/sysRole/findAll        浏览器上看到的是所有的json数据，但是日期格式有问题
    //1 查询所有记录
    @GetMapping("findAll")
    public List<SysRole> findAllRole() {
        //调用service
        List<SysRole> list = sysRoleService.list();
        return list;
    }
}



