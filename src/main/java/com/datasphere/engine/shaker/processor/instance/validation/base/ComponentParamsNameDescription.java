package com.datasphere.engine.shaker.processor.instance.validation.base;

public class ComponentParamsNameDescription {
	// FPGrowthValidationInstances
	public static final String fpgrowth_min_support = "最小支持";
	public static final String aprior_min_support = "最小支持";
	public static final String JoinCondition = "连接方式";
	public static final String JoinOn = "关联条件";
	public static final String JoinOutColumn = "输出列";
	
	public static final String kmeans_n_clusters = "聚类数";
	public static final String kmeans_init = "质心初始化方法";
	public static final String kmeans_max_iter = "最大迭代次数";
	
	
	public static final String random_n_estimators = "森林中树的个数";
	public static final String random_criterion = "裂分标准";
	public static final String random_max_depth = "树的最大深度";
	public static final String random_max_features = "树的最大特征数";
	public static final String random_min_samples_split = "叶结点裂分最小个数";
	public static final String random_min_samples_leaf = "叶结点数据最小个数";
	
	
	public static final String split_percentage = "切分比例";
	public static final String split_AttributedIndices = "目标列";
	
	public static final String xgboost_eta = "学习速率";
	public static final String xgboost_min_child_weight = "最小叶节点权重";
	public static final String xgboost_max_depth = "树的最大深度";
	public static final String xgboost_gamma = "裂分时损失最小下降值";
	public static final String xgboost_subsample = "采样比例";
	public static final String xgboost_colsample_bytree = "单树的特征选择比例";

}
