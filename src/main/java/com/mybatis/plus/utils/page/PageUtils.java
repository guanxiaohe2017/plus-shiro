

package com.mybatis.plus.utils.page;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * 描述: 分页工具类
 *
 * @author 官萧何
 * @date 2020/8/17 11:31
 * @version V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
public class PageUtils implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 总记录数
	 */
	private int totalCount;
	/**
	 * 每页记录数
	 */
	private int pageSize;
	/**
	 * 总页数
	 */
	private int totalPage;
	/**
	 * 当前页数
	 */
	private int currPage;
	/**
	 * 列表数据
	 */
	private List<?> list;

	/**
	 * 分页
	 * @param list        列表数据
	 * @param totalCount  总记录数
	 * @param pageSize    每页记录数
	 * @param currPage    当前页数
	 */
	public PageUtils(List<?> list, int totalCount, int pageSize, int currPage) {
		this.list = list;
		this.totalCount = totalCount;
		this.pageSize = pageSize;
		this.currPage = currPage;
		this.totalPage = (int)Math.ceil((double)totalCount/pageSize);
	}

	/**
	 * 分页
	 */
	public PageUtils(IPage<?> page) {
		this.list = page.getRecords();
		this.totalCount = (int)page.getTotal();
		this.pageSize = (int)page.getSize();
		this.currPage = (int)page.getCurrent();
		this.totalPage = (int)page.getPages();
	}

	public static PageUtils getPage(Map<String, Object> params, List<?> list) {
		IPage<Object> page = new Query<>().getPage(params);
		int current = Math.toIntExact(page.getCurrent());
		int size = Math.toIntExact(page.getSize());
		if (null == list) {
			return new PageUtils(new ArrayList<>(),
					0, size, current);
		}
		List<?> handleList = subList(list, current, size);
		return new PageUtils(handleList,
				list.size(), size, current);
	}

	private static List<?> subList(List<?> list, int current, int size) {
		int start = (current - 1) * size;
		int end = start + size;
		int listIndex = list.size() - 1;
		if (start > listIndex) {
			list = new ArrayList<>();
		} else {
			if (end > list.size()) {
				end = list.size();
			}
			list = list.subList(start, end);
		}
		return list;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getCurrPage() {
		return currPage;
	}

	public void setCurrPage(int currPage) {
		this.currPage = currPage;
	}

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

}
