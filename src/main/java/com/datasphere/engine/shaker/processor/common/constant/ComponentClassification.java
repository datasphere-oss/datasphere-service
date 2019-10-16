/*
 * Copyright 2019, Huahuidata, Inc.
 * DataSphere is licensed under the Mulan PSL v1.
 * You can use this software according to the terms and conditions of the Mulan PSL v1.
 * You may obtain a copy of Mulan PSL v1 at:
 * http://license.coscl.org.cn/MulanPSL
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND, EITHER EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT, MERCHANTABILITY OR FIT FOR A PARTICULAR
 * PURPOSE.
 * See the Mulan PSL v1 for more details.
 */

package com.datasphere.engine.shaker.processor.common.constant;

import com.datasphere.core.common.utils.O;
/**
 * 处理组件分类
 * @author theseusyang
 *
 */
public enum ComponentClassification {
	Normalize,				// 归一化
	Standardize,				// 标准化
	Split,					// 拆分
	Discretize,				// 离散化
	ConvertTo,				// 类型转换
	Resample,				// 随机采样
	ReplaceMissingValues,	// 缺失值填充
	Compute,					// 算术运算
	RandomForest,			// 随机森林
	NaiveBayes,				// 朴素贝叶斯
	SVM,						// 支持向量机
	XGBoost,					// XGBoost
	FPGrowth,				// FPGrowth
	ChangeType,				// 数据类型转换
	Rounding,				// 取整
	FindAndReplace,			// 查找与替换
	OneHotEncoding,			// 将目标列转换成One-hot形式
	UniqueValue,				// 唯一值查看
	MismatchProcess,			// 异常值处理
	
	SingleTTest,				// 单样本T检验
	DoubleTTest,				// 双样本T检验
	Covariance,				// 协方差分析
	
	LinearRegression,		// 多元线性回归
	LogicRegression,			// 逻辑回归
	DecisionTree,			// 决策树算法
	Apriori,					// Apriori，关联规则的一种
	Kmeans,					// Kmeans

	DataFilter,				//数据过滤
	DataPreProcess,			//子流程节点


	SimpleDataSource,
	Filter,					//查询
	Join,					//交集字段	(关联表)
	Diff,					//差集字段
	LeftJoin,				//横向并表
	Union,					//纵向并表
	TopN,					//Top排序
	GroupBy,					//分组统计
	FieldMapper,				//字典映射
	Tag;						//标签

	
	public static ComponentClassification from(String s) {
		return Enum.valueOf(ComponentClassification.class, s);
	}
	
}
