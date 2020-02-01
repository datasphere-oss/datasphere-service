import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div ng-controller=\"EvaluationRecipeEditor\">
    +             <div className=\"top-level-tabs objecttype-recipe\">
    +                 <div className=\"horizontal-flex row-fluid object-nav\">
    +                     <div className=\"flex oh\" std-object-breadcrumb=\"\">
    +                         <div className=\"noflex\">
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab == 'summary'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'summary';
    +                                 }}>
    +                                 概要
    +                             </a>
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab == 'settings'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'settings';
    +                                 }}>
    +                                 设置
    +                             </a>
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab == 'io'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'io';
    +                                 }}>
    +                                 输入/输出
    +                             </a>
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab  == 'advanced'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'advanced';
    +                                 }}>
    +                                 高级
    +                             </a>
    +                             <a
    +                                 className=\"{'enabled': topNav.tab == 'gitlog'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'gitlog';
    +                                 }}>
    +                                 历史
    +                             </a>
    +                             <div className=\"otherLinks\">
    +                                 <div discussions-button=\"\">
    +                                     <div
    +                                         style=\"display: inline-block\"
    +                                         include-no-scope=\"/templates/recipes/fragments/recipe-save-button.html\">
    +                                         <div
    +                                             style=\"display: inline-block\"
    +                                             include-no-scope=\"/templates/recipes/fragments/recipe-tabs-other-links.html\"
    +                                         />
    +                                     </div>
    +                                 </div>
    +                             </div>
    + 
    +                             <div include-no-scope=\"/templates/recipes/fragments/recipe-summary-tab.html\">
    +                                 <div include-no-scope=\"/templates/recipes/fragments/recipe-io-tab.html\">
    +                                     <div include-no-scope=\"/templates/recipes/fragments/recipe-git-log.html\">
    +                                         {topNav.tab == 'settings' && computablesMap ? (
    +                                             <div className=\"summary-tab dss-page\">
    +                                                 <div block-api-error=\"\">
    +                                                     <div className=\"h100 vertical-flex\">
    +                                                         <div className=\"flex\">
    +                                                             <div className=\"fh\">
    +                                                                 <div className=\"h100 oa\">
    +                                                                     <div className=\"horizontal-centerer\">
    +                                                                         <div style=\"background-color: #ffffff\">
    +                                                                             {recipeStatus.allMessagesForFrontend
    +                                                                                 .anyMessage ? (
    +                                                                                 <div style=\"padding: 10px\">
    +                                                                                     <div info-messages-raw-list-with-alert=\"recipeStatus.allMessagesForFrontend\" />
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                             {isBinaryClassification() &&
    +                                                                             isProbaAware() &&
    +                                                                             model.miniTask.backendType != 'VERTICA' ? (
    +                                                                                 <div className=\"recipe-settings-section1 w800\">
    +                                                                                     <h1 className=\"recipe-settings-section1-title\">
    +                                                                                         阀值
    +                                                                                     </h1>
    +                                                                                     <form className=\"dkuform-horizontal recipe-settings-section2\">
    +                                                                                         <div className=\"control-group\">
    +                                                                                             <label
    +                                                                                                 htmlFor=\"\"
    +                                                                                                 className=\"control-label\">
    +                                                                                                 阀值
    +                                                                                             </label>
    +                                                                                             <div className=\"controls\">
    +                                                                                                 <label>
    +                                                                                                     <input
    +                                                                                                         type=\"radio\"
    +                                                                                                         value=\"zeFalse\"
    +                                                                                                         value={
    +                                                                                                             desc.overrideModelSpecifiedThreshold
    +                                                                                                         }
    +                                                                                                     />
    +                                                                                                     使用当前版本模型的阈值
    +                                                                                                     ({
    +                                                                                                         modelDetails
    +                                                                                                             .userMeta
    +                                                                                                             .activeClassifierThreshold
    +                                                                                                     })
    +                                                                                                 </label>
    +                                                                                                 <label>
    +                                                                                                     <input
    +                                                                                                         type=\"radio\"
    +                                                                                                         value=\"zeTrue\"
    +                                                                                                         value={
    +                                                                                                             desc.overrideModelSpecifiedThreshold
    +                                                                                                         }
    +                                                                                                     />
    +                                                                                                     覆盖模型的阈值
    +                                                                                                 </label>
    +                                                                                             </div>
    +                                                                                         </div>
    +                                                                                         {desc.overrideModelSpecifiedThreshold ? (
    +                                                                                             <div className=\"control-group\">
    +                                                                                                 <label
    +                                                                                                     htmlFor=\"\"
    +                                                                                                     className=\"control-label\">
    +                                                                                                     覆盖, 用
    +                                                                                                 </label>
    +                                                                                                 <div className=\"controls\">
    +                                                                                                     <input
    +                                                                                                         type=\"number\"
    +                                                                                                         value={
    +                                                                                                             desc.forcedClassifierThreshold
    +                                                                                                         }
    +                                                                                                     />
    +                                                                                                 </div>
    +                                                                                             </div>
    +                                                                                         ) : null}
    +                                                                                     </form>
    +                                                                                 </div>
    +                                                                             ) : null}
    + 
    +                                                                             <div className=\"recipe-settings-section1 w800\">
    +                                                                                 <h1 className=\"recipe-settings-section1-title\">
    +                                                                                     输出
    +                                                                                 </h1>
    +                                                                                 <div className=\"recipe-settings-section2\">
    +                                                                                     <form className=\"dkuform-horizontal\">
    +                                                                                         {isProbaAware() ? (
    +                                                                                             <div>
    +                                                                                                 <div className=\"control-group\">
    +                                                                                                     <label className=\"control-label\">
    +                                                                                                         输出 概率
    +                                                                                                     </label>
    +                                                                                                     <div className=\"controls\">
    +                                                                                                         <label>
    +                                                                                                             <input
    +                                                                                                                 type=\"checkbox\"
    +                                                                                                                 value={
    +                                                                                                                     desc.outputProbabilities
    +                                                                                                                 }
    +                                                                                                             />
    +                                                                                                             <div className=\"help-inline\">
    +                                                                                                                 除了预测之外，每个类的输出概率
    +                                                                                                             </div>
    +                                                                                                         </label>
    +                                                                                                     </div>
    +                                                                                                 </div>
    +                                                                                                 {model.miniTask
    +                                                                                                     .backendType !=
    +                                                                                                     'VERTICA' &&
    +                                                                                                 selectedEngine() !=
    +                                                                                                     'SQL' &&
    +                                                                                                 isBinaryClassification() ? (
    +                                                                                                     <div className=\"control-group\">
    +                                                                                                         <label className=\"control-label\">
    +                                                                                                             输出 百分比
    +                                                                                                         </label>
    +                                                                                                         <div className=\"controls\">
    +                                                                                                             <label>
    +                                                                                                                 <input
    +                                                                                                                     type=\"checkbox\"
    +                                                                                                                     value={
    +                                                                                                                         desc.outputProbaPercentiles
    +                                                                                                                     }
    +                                                                                                                 />
    +                                                                                                                 <div className=\"help-inline\">
    +                                                                                                                     百分比,
    +                                                                                                                     在测试集中,
    +                                                                                                                     class
    +                                                                                                                     1
    +                                                                                                                     的问题
    +                                                                                                                 </div>
    +                                                                                                             </label>
    +                                                                                                         </div>
    +                                                                                                     </div>
    +                                                                                                 ) : null}
    +                                                                                             </div>
    +                                                                                         ) : null}
    +                                                                                         {computablesMap ? (
    +                                                                                             <div>
    +                                                                                                 {preparedInputSchema ? (
    +                                                                                                     <div scoring-columns-filter=\"\" />
    +                                                                                                 ) : null}
    +                                                                                                 {modelDetails ? (
    +                                                                                                     <div>
    +                                                                                                         <div metrics-filter=\"\" />
    +                                                                                                         {modelDetails
    +                                                                                                             .coreParams
    +                                                                                                             .weight
    +                                                                                                             .weightMethod ===
    +                                                                                                         'SAMPLE_WEIGHT' ? (
    +                                                                                                             <div className=\"doctor-explanation\">
    +                                                                                                                 注意:
    +                                                                                                                 指标是使用变量计算的{' '}
    +                                                                                                                 <strong>
    +                                                                                                                     {
    +                                                                                                                         modelDetails
    +                                                                                                                             .coreParams
    +                                                                                                                             .weight
    +                                                                                                                             .sampleWeightVariable
    +                                                                                                                     }
    +                                                                                                                 </strong>{' '}
    +                                                                                                                 作为样本权重.
    +                                                                                                             </div>
    +                                                                                                         ) : null}
    +                                                                                                     </div>
    +                                                                                                 ) : null}
    +                                                                                             </div>
    +                                                                                         ) : null}
    +                                                                                     </form>
    + 
    +                                                                                     {isBinaryClassification() ===
    +                                                                                         false &&
    +                                                                                     isProbaAware() === false ? (
    +                                                                                         <div className=\"recipe-settings-section1 w800\">
    +                                                                                             <p className=\"recipe-settings-section2\">
    +                                                                                                 没有可用于回归评分的设置
    +                                                                                             </p>
    +                                                                                         </div>
    +                                                                                     ) : null}
    + 
    +                                                                                     {willUseSpark() ? (
    +                                                                                         <div className=\"recipe-settings-section1 w800\">
    +                                                                                             <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                 Spark
    +                                                                                             </h1>
    +                                                                                             <div className=\"recipe-settings-section2\">
    +                                                                                                 <div
    +                                                                                                     spark-override-config=\"\"
    +                                                                                                     config=\"desc.sparkParams.sparkConf\"
    +                                                                                                     task=\"desc\"
    +                                                                                                     task-type=\"MLLib\"
    +                                                                                                 />
    +                                                                                             </div>
    + 
    +                                                                                             {mayUseContainer() ? (
    +                                                                                                 <div className=\"recipe-settings-section1 w800\">
    +                                                                                                     <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                         容器配置
    +                                                                                                     </h1>
    +                                                                                                     <div
    +                                                                                                         className=\"recipe-settings-section2\"
    +                                                                                                         container-selection-form=\"recipe.params.containerSelection\"
    +                                                                                                     />
    +                                                                                                 </div>
    +                                                                                             ) : null}
    +                                                                                         </div>
    +                                                                                     ) : null}
    +                                                                                 </div>
    +                                                                             </div>
    +                                                                         </div>
    + 
    +                                                                         {valCtx.preRunValidationError ||
    +                                                                         startedJob.jobId ? (
    +                                                                             <div className=\"noflex job-result-pane\">
    +                                                                                 <div className=\"recipe-settings-floating-result\">
    +                                                                                     <div include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-result.html\" />
    +                                                                                 </div>
    +                                                                             </div>
    +                                                                         ) : null}
    +                                                                     </div>
    + 
    +                                                                     <div className=\"recipe-settings-floating-run\">
    +                                                                         <div include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-partitions.html\" />
    +                                                                         <div include-no-scope=\"/templates/recipes/fragments/run-job-buttons.html\" />
    +                                                                     </div>
    +                                                                 </div>
    + 
    +                                                                 {model.miniTask.backendType !== 'KERAS' ? (
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/limits-advanced-tab.html\">
    +                                                                         {model.miniTask.backendType === 'KERAS' ? (
    +                                                                             <div include-no-scope=\"/templates/recipes/fragments/keras-scoring-evaluation-prediction-advanced-tab.html\" />
    +                                                                         ) : null}
    +                                                                     </div>
    +                                                                 ) : null}
    +                                                             </div>
    +                                                         </div>
    +                                                     </div>
    +                                                 </div>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;