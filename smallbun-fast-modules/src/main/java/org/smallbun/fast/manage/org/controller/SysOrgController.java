/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2019 ‭‭‭‭‭‭‭‭‭‭‭‭[smallbun] www.smallbun.org
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.smallbun.fast.manage.org.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.smallbun.fast.common.PageFactory;
import org.smallbun.fast.common.utils.AutoMapperUtil;
import org.smallbun.fast.manage.org.entity.SysOrgEntity;
import org.smallbun.fast.manage.org.service.SysOrgService;
import org.smallbun.fast.manage.org.vo.SysOrgVO;
import org.smallbun.framework.annotation.AutoQueryDictValue;
import org.smallbun.framework.annotation.DemoEnvironment;
import org.smallbun.framework.base.BaseController;
import org.smallbun.framework.result.AjaxResult;
import org.smallbun.framework.result.PageableResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

import static org.smallbun.framework.constant.UrlPrefixConstant.UNIQUE;
import static org.smallbun.framework.toolkit.AutoMapperUtil.mappingList;

/**
 * 部门控制器
 *
 * @author SanLi
 * Created by 2689170096@qq.com on 2018/8/3
 */
@Validated
@RestController
@RequestMapping(value = "/org")
public class SysOrgController extends BaseController {
	@Autowired
	public SysOrgController(SysOrgService sysOrgService) {
		this.sysOrgService = sysOrgService;
	}

	@ModelAttribute
	public SysOrgVO model(HttpServletRequest request) {
		return sysOrgService.model(request);
	}

	/**
	 * 列表页面
	 *
	 * @return ModelAndView
	 */
	@GetMapping(value = {"/", ""})
	public ModelAndView org() {
		return new ModelAndView("/modules/manage/org/org_list.html");
	}

	/**
	 * form 表单
	 *
	 * @param org   SysOrgEntity
	 * @param model Model
	 * @return ModelAndView
	 */
	@GetMapping(value = "form")
	@PreAuthorize("hasAuthority('manage:org:add') or hasAuthority('manage:org:add')")
	public ModelAndView form(SysOrgVO org, Model model) {
		model.addAttribute("org", org);
		return new ModelAndView("/modules/manage/org/org_form.html");
	}

	/**
	 * 添加一条记录
	 *
	 * @param org 实体类对象
	 * @return AjaxResult
	 */
	@DemoEnvironment
	@PostMapping("/saveOrUpdate")
	@PreAuthorize("hasAuthority('manage:notify:add') or hasAuthority('manage:notify:add')")
	public AjaxResult saveOrUpdate(@Valid SysOrgVO org) {
		return AjaxResult.builder().result(sysOrgService.saveOrUpdate(org)).build();
	}

	/**
	 * 根据ID删除一条记录
	 *
	 * @param id 主键ID
	 * @return AjaxResult
	 */
	@DemoEnvironment
	@PostMapping("/removeById")
	@PreAuthorize("hasAuthority('manage:org:del')")
	public AjaxResult removeById(
			@NotBlank(message = "id不能为空") @RequestParam(value = "id", required = false) String id) {
		return AjaxResult.builder().result(sysOrgService.removeById(id)).build();
	}

	/**
	 * 批量删除一条记录
	 *
	 * @param ids ID列表
	 * @return AjaxResult
	 */
	@DemoEnvironment
	@PostMapping("/removeByIds")
	@PreAuthorize("hasAuthority('manage:org:del')")
	public AjaxResult removeByIds(
			@NotBlank(message = "id不能为空") @RequestParam(value = "ids", required = false) List<String> ids) {
		return AjaxResult.builder().result(sysOrgService.removeByIds(ids)).build();
	}

	/**
	 * 分页查询
	 *
	 * @param page page
	 * @return PageableResult
	 */
	@PostMapping(value = "/page")
	@AutoQueryDictValue
	public PageableResult page(Page<SysOrgEntity> page, SysOrgVO vo) {
		return PageableResult.builder().page(pageVOFilling(
				sysOrgService.page(new PageFactory<SysOrgEntity>().defaultPage(page), new QueryWrapper<>(vo)),
				SysOrgVO.class)).build();
	}

	/**
	 * 查询全部数据
	 *
	 * @return AjaxResult
	 */
	@PostMapping(value = "/list")
	@AutoQueryDictValue
	public AjaxResult list() {
		return AjaxResult.builder().result(excludeZtreeChildrenField(
				mappingList(sysOrgService.list(new QueryWrapper<>()), new ArrayList<SysOrgVO>(), SysOrgVO.class)))
				.build();
	}

	/**
	 * 返回树
	 *
	 * @return AjaxResult
	 */
	@PostMapping(value = "/tree")
	public AjaxResult tree() {
		return AjaxResult.builder().result(AutoMapperUtil
				.mappingTreeList(sysOrgService.tree(new QueryWrapper<>()), new ArrayList<>(), SysOrgVO.class)).build();
	}

	/**
	 * 唯一
	 * @param vo
	 * @return
	 */
	@RequestMapping(value = UNIQUE)
	public AjaxResult unique(SysOrgVO vo) {
		return AjaxResult.builder().result(sysOrgService.unique(vo)).build();
	}

	/**
	 * 注入部门业务接口
	 */
	private final SysOrgService sysOrgService;
}
