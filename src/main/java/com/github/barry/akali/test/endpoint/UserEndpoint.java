package com.github.barry.akali.test.endpoint;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.barry.akali.base.BaseEndpoint;
import com.github.barry.akali.base.utils.PageInfo;
import com.github.barry.akali.base.utils.RequestSearchUtils;
import com.github.barry.akali.base.dto.ResponseDto;
import com.github.barry.akali.test.dto.request.UserRequestDto;
import com.github.barry.akali.test.dto.response.UserResponseDto;
import com.github.barry.akali.test.service.UserService;

/**
 * controller for User
 * 这是类的注释
 *
 * @author quansr
 * Created On 2020-04-03.
 */
@RestController
@RequestMapping("/user")
public class UserEndpoint extends BaseEndpoint {

    @Autowired
    private UserService userService;

    /**
     * 新增
     * 
     * @param userRequestDto
     * @return
     */
    @PostMapping("")
    public ResponseEntity<?> create(@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResp = userService.create(userRequestDto);
        return ResponseEntity.ok(new EntityModel<UserResponseDto>(userResp, super.getSelfLink(this.getClass(), userResp.getId())));
    }

    /**
     * 删除
     * 
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseDto<?> delete(@PathVariable Integer id) {
        userService.deleteById(id);
        return ResponseDto.success(null);
    }

    /**
     * 更新
     * 
     * @param UserRequestDto
     * @param id
     * @return
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id,@RequestBody UserRequestDto userRequestDto) {
        UserResponseDto userResp = userService.update(id,userRequestDto);
        return ResponseEntity.ok(new EntityModel<UserResponseDto>(userResp, super.getSelfLink(this.getClass(), userResp.getId())));
    }

    /**
     * 详情
     * 
     * @param id
     * @return
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Integer id) {
        UserResponseDto userResp = userService.details(id);
        return ResponseEntity.ok(new EntityModel<UserResponseDto>(userResp, super.getSelfLink(this.getClass(), userResp.getId())));
    }

    @Override
    @GetMapping("")
    public HttpEntity<PagedModel<EntityModel<?>>> getPageData(
            @RequestParam(value = PAGE_NUM, defaultValue = PAGE_NUM_VAL) int pageNumber,
            @RequestParam(value = PAGE_SIZE, defaultValue = PAGE_SIZE_VAL) int pageSize,
            @RequestParam(value = SORTTYPE, defaultValue = SORT_TYPE_VAL) String sortType, ServletRequest request) {
        // 获取搜索参数
        Map<String, Object> searchParams = RequestSearchUtils.getParamStartWith(request, SEARCH_PREFIX1);
        PageInfo pageInfo = new PageInfo(pageNumber, pageSize, sortType);
        Page<UserResponseDto> page = userService.getPageList(searchParams, pageInfo);
        return super.doPage(pageNumber, pageSize, sortType, request, this.getClass(), page);
    }

    /**
     * 条件搜索，返回不分页的列表
     * 
     * @param request
     * @return
     */
    @GetMapping("/find/params")
    public ResponseEntity<?> findByParams(ServletRequest request) {
        // 1.获取搜索参数
        Map<String, Object> searchParams = RequestSearchUtils.getParamStartWith(request, SEARCH_PREFIX1);
        // 2.获取数据
        List<UserResponseDto> dataList = userService.findByParams(searchParams);
        return super.doListResources(dataList, this.getClass());
    }
}