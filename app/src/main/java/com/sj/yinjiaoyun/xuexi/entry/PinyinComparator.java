package com.sj.yinjiaoyun.xuexi.entry;



import com.sj.yinjiaoyun.xuexi.domain.College;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 * 排序工具类
 *
 */
public class PinyinComparator implements Comparator<College> {

	public int compare(College o1, College o2) {
		if (o1.getFirstSpell().equals("@")
				|| o2.getFirstSpell().equals("#")) {
			return -1;
		} else if (o1.getFirstSpell().equals("#")
				|| o2.getFirstSpell().equals("@")) {
			return 1;
		} else {
			return o1.getFirstSpell().compareTo(o2.getFirstSpell());
		}
	}

}
