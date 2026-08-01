# 编辑的内容

## 新 配置分页插件，该插件通过配置类实现

    E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step3\common\service-util\src\main\java\com\rc\system\config\MybatisPlusConfig.java


## 添加 分页接口

    * 修改 controller文件，添加分页相关代码；

            ```java
            import com.baomidou.mybatisplus.core.metadata.IPage;
            import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
            import com.rc.model.vo.SysRoleQueryVo;


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
            ```

    * 修改 service接口文件 以及 其实现类的文件，添加分页相关代码；

            ```java
            import com.baomidou.mybatisplus.core.metadata.IPage;
            import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
            import com.rc.model.vo.SysRoleQueryVo;

            //条件分页查询
            IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo);



            @Service
            public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {
                //条件分页查询
                @Override
                public IPage<SysRole> selectPage(Page<SysRole> pageParam, SysRoleQueryVo sysRoleQueryVo) {
                // 需要在 mapper 中创建 selectPage ，否则 baseMapper.selectPage(pageParam,sysRoleQueryVo) 会报警告
                IPage<SysRole> pageModel = baseMapper.selectPage(pageParam, sysRoleQueryVo);
                return pageModel;
                }
            }
            ```

    * 创建 mapper 的 xml文件 E:\Project\AAA_All_MINE\all-java-backend\guigu-auth-parent-learn\step3\service-system\src\main\resources\mapper\SysRoleMapper.xml
    
    