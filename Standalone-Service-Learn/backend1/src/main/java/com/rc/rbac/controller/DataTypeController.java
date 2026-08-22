package com.rc.rbac.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rc.rbac.common.PageResult;
import com.rc.rbac.common.Result;
import com.rc.rbac.dto.request.MenuUpdateRequest;
import com.rc.rbac.dto.response.*;
import com.rc.rbac.entity.DataTypeDemo;
import com.rc.rbac.entity.Demo;
import com.rc.rbac.entity.Menu;
import com.rc.rbac.entity.Role;
import com.rc.rbac.mapper.RoleMapper;
import com.rc.rbac.mapper.UserMapper;
import com.rc.rbac.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据类型示例控制器
 * 每个接口演示一种可向前端返回的数据类型，数据均来自数据库
 */
@Tag(name = "数据类型示例", description = "每种数据类型一个接口")
@RestController
@RequestMapping("/api/data-type")
@RequiredArgsConstructor
public class DataTypeController {

    private final DataTypeDemoService dataTypeDemoService;
    private final DemoService demoService;
    private final UserService userService;
    private final RoleService roleService;
    private final MenuService menuService;
    private final RoleMapper roleMapper;
    private final UserMapper userMapper;

    private DataTypeDemo getRecord(Long id) {
        return dataTypeDemoService.getDataTypeDemoById(id);
    }

    @Operation(summary = "String 字符串")
    @GetMapping("/string")
    public Result<String> getString(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getStringVal());
    }

    @Operation(summary = "修改 String 字符串")
    @PutMapping("/string")
    public Result<String> updateString(@RequestParam(defaultValue = "1") Long id, @RequestBody String value) {
        DataTypeDemo record = getRecord(id);
        record.setStringVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getStringVal());
    }

    @Operation(summary = "Integer 整数")
    @GetMapping("/integer")
    public Result<Integer> getInteger(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getIntegerVal());
    }

    @Operation(summary = "修改 Integer 整数")
    @PutMapping("/integer")
    public Result<Integer> updateInteger(@RequestParam(defaultValue = "1") Long id, @RequestBody Integer value) {
        DataTypeDemo record = getRecord(id);
        record.setIntegerVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getIntegerVal());
    }

    @Operation(summary = "Long 长整数")
    @GetMapping("/long")
    public Result<Long> getLong(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getLongVal());
    }

    @Operation(summary = "修改 Long 长整数")
    @PutMapping("/long")
    public Result<Long> updateLong(@RequestParam(defaultValue = "1") Long id, @RequestBody Long value) {
        DataTypeDemo record = getRecord(id);
        record.setLongVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getLongVal());
    }

    @Operation(summary = "Float 单精度浮点")
    @GetMapping("/float")
    public Result<Float> getFloat(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getFloatVal());
    }

    @Operation(summary = "修改 Float 单精度浮点")
    @PutMapping("/float")
    public Result<Float> updateFloat(@RequestParam(defaultValue = "1") Long id, @RequestBody Float value) {
        DataTypeDemo record = getRecord(id);
        record.setFloatVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getFloatVal());
    }

    @Operation(summary = "Double 双精度浮点")
    @GetMapping("/double")
    public Result<Double> getDouble(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getDoubleVal());
    }

    @Operation(summary = "修改 Double 双精度浮点")
    @PutMapping("/double")
    public Result<Double> updateDouble(@RequestParam(defaultValue = "1") Long id, @RequestBody Double value) {
        DataTypeDemo record = getRecord(id);
        record.setDoubleVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getDoubleVal());
    }

    @Operation(summary = "Boolean 布尔")
    @GetMapping("/boolean")
    public Result<Boolean> getBoolean(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getBooleanVal());
    }

    @Operation(summary = "修改 Boolean 布尔")
    @PutMapping("/boolean")
    public Result<Boolean> updateBoolean(@RequestParam(defaultValue = "1") Long id, @RequestBody Boolean value) {
        DataTypeDemo record = getRecord(id);
        record.setBooleanVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getBooleanVal());
    }

    @Operation(summary = "BigDecimal 高精度数值")
    @GetMapping("/bigdecimal")
    public Result<java.math.BigDecimal> getBigDecimal(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getDecimalVal());
    }

    @Operation(summary = "修改 BigDecimal 高精度数值")
    @PutMapping("/bigdecimal")
    public Result<java.math.BigDecimal> updateBigDecimal(@RequestParam(defaultValue = "1") Long id, @RequestBody java.math.BigDecimal value) {
        DataTypeDemo record = getRecord(id);
        record.setDecimalVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getDecimalVal());
    }

    @Operation(summary = "LocalDate 日期")
    @GetMapping("/localdate")
    public Result<java.time.LocalDate> getLocalDate(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getDateVal());
    }

    @Operation(summary = "修改 LocalDate 日期")
    @PutMapping("/localdate")
    public Result<java.time.LocalDate> updateLocalDate(@RequestParam(defaultValue = "1") Long id, @RequestBody java.time.LocalDate value) {
        DataTypeDemo record = getRecord(id);
        record.setDateVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getDateVal());
    }

    @Operation(summary = "LocalDateTime 日期时间")
    @GetMapping("/localdatetime")
    public Result<java.time.LocalDateTime> getLocalDateTime(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(record.getDatetimeVal());
    }

    @Operation(summary = "修改 LocalDateTime 日期时间")
    @PutMapping("/localdatetime")
    public Result<java.time.LocalDateTime> updateLocalDateTime(@RequestParam(defaultValue = "1") Long id, @RequestBody java.time.LocalDateTime value) {
        DataTypeDemo record = getRecord(id);
        record.setDatetimeVal(value);
        dataTypeDemoService.updateById(record);
        return Result.success(record.getDatetimeVal());
    }

    @Operation(summary = "List 列表")
    @GetMapping("/list")
    public Result<List<String>> getList(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(parseList(record.getListVal()));
    }

    @Operation(summary = "修改 List 列表")
    @PutMapping("/list")
    public Result<List<String>> updateList(@RequestParam(defaultValue = "1") Long id, @RequestBody List<String> value) {
        DataTypeDemo record = getRecord(id);
        record.setListVal(value.toString());
        dataTypeDemoService.updateById(record);
        return Result.success(parseList(record.getListVal()));
    }

    @Operation(summary = "Set 集合")
    @GetMapping("/set")
    public Result<Set<Integer>> getSet(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        List<String> items = parseList(record.getSetVal());
        Set<Integer> set = new LinkedHashSet<>();
        for (String item : items) {
            set.add(Integer.parseInt(item));
        }
        return Result.success(set);
    }

    @Operation(summary = "修改 Set 集合")
    @PutMapping("/set")
    public Result<Set<Integer>> updateSet(@RequestParam(defaultValue = "1") Long id, @RequestBody Set<Integer> value) {
        DataTypeDemo record = getRecord(id);
        record.setSetVal(value.toString());
        dataTypeDemoService.updateById(record);
        List<String> items = parseList(record.getSetVal());
        Set<Integer> set = new LinkedHashSet<>();
        for (String item : items) {
            set.add(Integer.parseInt(item));
        }
        return Result.success(set);
    }

    @Operation(summary = "Map 映射")
    @GetMapping("/map")
    public Result<Map<String, Object>> getMap(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        return Result.success(parseMap(record.getMapVal()));
    }

    @Operation(summary = "修改 Map 映射")
    @PutMapping("/map")
    public Result<Map<String, Object>> updateMap(@RequestParam(defaultValue = "1") Long id, @RequestBody Map<String, Object> value) {
        DataTypeDemo record = getRecord(id);
        record.setMapVal(value.toString());
        dataTypeDemoService.updateById(record);
        return Result.success(parseMap(record.getMapVal()));
    }

    @Operation(summary = "数组")
    @GetMapping("/array")
    public Result<String[]> getArray(@RequestParam(defaultValue = "1") Long id) {
        DataTypeDemo record = getRecord(id);
        List<String> list = parseList(record.getArrayVal());
        return Result.success(list.toArray(new String[0]));
    }

    @Operation(summary = "修改 数组")
    @PutMapping("/array")
    public Result<String[]> updateArray(@RequestParam(defaultValue = "1") Long id, @RequestBody String[] value) {
        DataTypeDemo record = getRecord(id);
        record.setArrayVal(Arrays.toString(value));
        dataTypeDemoService.updateById(record);
        List<String> list = parseList(record.getArrayVal());
        return Result.success(list.toArray(new String[0]));
    }

    @Operation(summary = "Void 无返回数据")
    @GetMapping("/void")
    public Result<Void> getVoid() {
        return Result.success("操作成功", null);
    }

    @Operation(summary = "DemoResponse 对象")
    @GetMapping("/demo-response")
    public Result<DemoResponse> getDemoResponse(@RequestParam(defaultValue = "1") Long id) {
        Demo demo = demoService.getById(id);
        if (demo == null) {
            return Result.error("无数据");
        }
        DemoResponse resp = new DemoResponse();
        resp.setId(demo.getId());
        resp.setName(demo.getName());
        resp.setDescription(demo.getDescription());
        resp.setAge(demo.getAge());
        resp.setAmount(demo.getAmount());
        resp.setScore(demo.getScore());
        resp.setPrice(demo.getPrice());
        resp.setBigDecimal(demo.getBigDecimal());
        resp.setEnabled(demo.getEnabled());
        resp.setStatus(demo.getStatus());
        resp.setBirthday(demo.getBirthday());
        resp.setCreateTime(demo.getCreateTime());
        resp.setUpdateTime(demo.getUpdateTime());
        resp.setCreateBy(demo.getCreateBy());
        resp.setUpdateBy(demo.getUpdateBy());
        return Result.success(resp);
    }

    @Operation(summary = "修改 DemoResponse 对象")
    @PutMapping("/demo-response")
    public Result<DemoResponse> updateDemoResponse(@RequestParam(defaultValue = "1") Long id, @RequestBody Demo demo) {
        Demo existing = demoService.getById(id);
        if (existing == null) {
            return Result.error("无数据");
        }
        demo.setId(id);
        demoService.updateById(demo);
        Demo updated = demoService.getById(id);
        DemoResponse resp = new DemoResponse();
        resp.setId(updated.getId());
        resp.setName(updated.getName());
        resp.setDescription(updated.getDescription());
        resp.setAge(updated.getAge());
        resp.setAmount(updated.getAmount());
        resp.setScore(updated.getScore());
        resp.setPrice(updated.getPrice());
        resp.setBigDecimal(updated.getBigDecimal());
        resp.setEnabled(updated.getEnabled());
        resp.setStatus(updated.getStatus());
        resp.setBirthday(updated.getBirthday());
        resp.setCreateTime(updated.getCreateTime());
        resp.setUpdateTime(updated.getUpdateTime());
        resp.setCreateBy(updated.getCreateBy());
        resp.setUpdateBy(updated.getUpdateBy());
        return Result.success(resp);
    }

    @Operation(summary = "UserResponse 用户对象（含内部类列表）")
    @GetMapping("/user-response")
    public Result<UserResponse> getUserResponse(@RequestParam(defaultValue = "1") Long id) {
        UserResponse resp = userService.getUserById(id);
        return Result.success(resp);
    }

    @Operation(summary = "修改 UserResponse 用户对象")
    @PutMapping("/user-response")
    public Result<UserResponse> updateUserResponse(@RequestParam(defaultValue = "1") Long id, @RequestBody com.rc.rbac.entity.User user) {
        com.rc.rbac.entity.User existing = userService.getById(id);
        if (existing == null) {
            return Result.error("用户不存在");
        }
        user.setId(id);
        userService.updateById(user);
        UserResponse resp = userService.getUserById(id);
        return Result.success(resp);
    }

    @Operation(summary = "RoleResponse 角色对象")
    @GetMapping("/role-response")
    public Result<RoleResponse> getRoleResponse(@RequestParam(defaultValue = "1") Long id) {
        RoleResponse resp = roleService.getRoleById(id);
        return Result.success(resp);
    }

    @Operation(summary = "修改 RoleResponse 角色对象")
    @PutMapping("/role-response")
    public Result<RoleResponse> updateRoleResponse(@RequestParam(defaultValue = "1") Long id, @RequestBody Role role) {
        Role existing = roleService.getById(id);
        if (existing == null) {
            return Result.error("角色不存在");
        }
        role.setId(id);
        roleService.updateById(role);
        RoleResponse resp = roleService.getRoleById(id);
        return Result.success(resp);
    }

    @Operation(summary = "LoginResponse 登录响应（含内部类）")
    @GetMapping("/login-response")
    public Result<LoginResponse> getLoginResponse(@RequestParam(defaultValue = "1") Long id) {
        com.rc.rbac.entity.User user = userService.getById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }

        List<Role> roles = roleMapper.selectRolesByUserId(user.getId());
        List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        List<String> permissions = userMapper.selectPermissionsByUserId(user.getId());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        userInfo.setAvatar(user.getAvatar());
        userInfo.setRoles(roleKeys);
        userInfo.setPermissions(permissions);

        LoginResponse resp = new LoginResponse();
        resp.setToken("demo-token-from-database");
        resp.setUserInfo(userInfo);
        return Result.success(resp);
    }

    @Operation(summary = "修改 LoginResponse 用户基本信息")
    @PutMapping("/login-response")
    public Result<LoginResponse> updateLoginResponse(@RequestParam(defaultValue = "1") Long id, @RequestBody com.rc.rbac.entity.User user) {
        com.rc.rbac.entity.User existing = userService.getById(id);
        if (existing == null) {
            return Result.error("用户不存在");
        }
        user.setId(id);
        userService.updateById(user);

        com.rc.rbac.entity.User updated = userService.getById(id);
        List<Role> roles = roleMapper.selectRolesByUserId(updated.getId());
        List<String> roleKeys = roles.stream().map(Role::getRoleKey).collect(Collectors.toList());
        List<String> permissions = userMapper.selectPermissionsByUserId(updated.getId());

        LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo();
        userInfo.setId(updated.getId());
        userInfo.setUsername(updated.getUsername());
        userInfo.setNickname(updated.getNickname());
        userInfo.setAvatar(updated.getAvatar());
        userInfo.setRoles(roleKeys);
        userInfo.setPermissions(permissions);

        LoginResponse resp = new LoginResponse();
        resp.setToken("demo-token-from-database");
        resp.setUserInfo(userInfo);
        return Result.success(resp);
    }

    @Operation(summary = "MenuTreeResponse 菜单树（含递归子节点）")
    @GetMapping("/menu-tree-response")
    public Result<MenuTreeResponse> getMenuTreeResponse(@RequestParam(defaultValue = "1") Long id) {
        List<MenuTreeResponse> tree = menuService.getMenuTree();
        if (tree.isEmpty()) {
            return Result.error("无菜单数据");
        }
        MenuTreeResponse target = findMenuById(tree, id);
        if (target == null) {
            return Result.error("未找到指定菜单");
        }
        return Result.success(target);
    }

    @Operation(summary = "修改 MenuTreeResponse 菜单")
    @PutMapping("/menu-tree-response")
    public Result<MenuTreeResponse> updateMenuTreeResponse(@RequestParam(defaultValue = "1") Long id, @RequestBody MenuUpdateRequest request) {
        Menu existing = menuService.getMenuById(id);
        if (existing == null) {
            return Result.error("菜单不存在");
        }
        menuService.updateMenu(id, request);
        List<MenuTreeResponse> tree = menuService.getMenuTree();
        MenuTreeResponse target = findMenuById(tree, id);
        return Result.success(target);
    }

    private MenuTreeResponse findMenuById(List<MenuTreeResponse> menuList, Long id) {
        for (MenuTreeResponse menu : menuList) {
            if (id.equals(menu.getId())) {
                return menu;
            }
            if (menu.getChildren() != null && !menu.getChildren().isEmpty()) {
                MenuTreeResponse found = findMenuById(menu.getChildren(), id);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    @Operation(summary = "PageResult 分页结果")
    @GetMapping("/page-result")
    public Result<PageResult<DemoResponse>> getPageResult(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "5") Integer pageSize) {
        PageResult<DemoResponse> pageResult = demoService.getDemoPage(pageNum, pageSize, null, null);
        return Result.success(pageResult);
    }

    @Operation(summary = "List<UserResponse> 用户列表")
    @GetMapping("/user-list")
    public Result<List<UserResponse>> getUserList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        PageResult<UserResponse> page = userService.getUserPage(pageNum, pageSize, null, null);
        return Result.success(page.getRecords());
    }

    @Operation(summary = "List<Map<String, Object>> Map列表")
    @GetMapping("/map-list")
    public Result<List<Map<String, Object>>> getMapList() {
        List<DataTypeDemo> records = dataTypeDemoService.list();
        List<Map<String, Object>> list = records.stream().map(record -> {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("id", record.getId());
            map.put("stringVal", record.getStringVal());
            map.put("integerVal", record.getIntegerVal());
            map.put("booleanVal", record.getBooleanVal());
            return map;
        }).collect(Collectors.toList());
        return Result.success(list);
    }

    @Operation(summary = "List<DemoResponse> Demo列表")
    @GetMapping("/demo-list")
    public Result<List<DemoResponse>> getDemoList() {
        List<Demo> demos = demoService.list(new LambdaQueryWrapper<Demo>()
                .orderByDesc(Demo::getCreateTime));
        List<DemoResponse> list = demos.stream().map(demo -> {
            DemoResponse resp = new DemoResponse();
            resp.setId(demo.getId());
            resp.setName(demo.getName());
            resp.setDescription(demo.getDescription());
            resp.setAge(demo.getAge());
            resp.setAmount(demo.getAmount());
            resp.setScore(demo.getScore());
            resp.setPrice(demo.getPrice());
            resp.setBigDecimal(demo.getBigDecimal());
            resp.setEnabled(demo.getEnabled());
            resp.setStatus(demo.getStatus());
            resp.setBirthday(demo.getBirthday());
            resp.setCreateTime(demo.getCreateTime());
            resp.setUpdateTime(demo.getUpdateTime());
            resp.setCreateBy(demo.getCreateBy());
            resp.setUpdateBy(demo.getUpdateBy());
            return resp;
        }).collect(Collectors.toList());
        return Result.success(list);
    }

    // ==================== JSON 解析工具方法 ====================

    private List<String> parseList(String json) {
        if (json == null || json.isEmpty() || json.equals("[]")) {
            return new ArrayList<>();
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("[")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("]")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        if (trimmed.isEmpty()) {
            return new ArrayList<>();
        }
        String[] items = trimmed.split(",");
        List<String> result = new ArrayList<>();
        for (String item : items) {
            String value = item.trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            result.add(value);
        }
        return result;
    }

    private Map<String, Object> parseMap(String json) {
        if (json == null || json.isEmpty() || json.equals("{}")) {
            return new HashMap<>();
        }
        String trimmed = json.trim();
        if (trimmed.startsWith("{")) {
            trimmed = trimmed.substring(1);
        }
        if (trimmed.endsWith("}")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }
        if (trimmed.isEmpty()) {
            return new HashMap<>();
        }
        Map<String, Object> map = new LinkedHashMap<>();
        String[] pairs = trimmed.split(",");
        for (String pair : pairs) {
            String[] kv = pair.split(":");
            if (kv.length == 2) {
                String key = kv[0].trim();
                String val = kv[1].trim();
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                if (val.startsWith("\"") && val.endsWith("\"")) {
                    map.put(key, val.substring(1, val.length() - 1));
                } else if ("true".equals(val)) {
                    map.put(key, true);
                } else if ("false".equals(val)) {
                    map.put(key, false);
                } else {
                    try {
                        map.put(key, Integer.parseInt(val));
                    } catch (NumberFormatException e) {
                        map.put(key, val);
                    }
                }
            }
        }
        return map;
    }
}
