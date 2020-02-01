import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div ng-controller=\"ClusteringClusterRecipeEditor\">
    +             <div className=\"top-level-tabs objecttype-recipe\">
    +                 <div className=\"horizontal-flex row-fluid object-nav\">
    +                     <div className=\"flex oh\" std-object-breadcrumb=\"\">
    +                         <div className=\"noflex\">
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab  == 'summary'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'summary';
    +                                 }}>
    +                                 概要
    +                             </a>
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab  == 'settings'}\"
    +                                 onClick={() => {
    +                                     topNav.tab = 'settings';
    +                                 }}>
    +                                 设置
    +                             </a>
    +                             <a
    +                                 className=\"{'tab': true, 'enabled': topNav.tab  == 'io'}\"
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
    +                                     {desc.generatingModelId ? (
    +                                         <div style=\"display: inline-block\">
    +                                             <a
    +                                                 className=\"btn btn-alt btn-analysis\"
    +                                                 onClick={() => {
    +                                                     goToAnalysisModel();
    +                                                 }}>
    +                                                 <i className=\"icon icon-dku-nav_analysis\">
    +                                                     <span>查看原有分析</span>
    +                                                 </i>
    +                                             </a>
    +                                             <i className=\"icon icon-dku-nav_analysis\" />
    +                                         </div>
    +                                     ) : null}
    +                                     <i className=\"icon icon-dku-nav_analysis\">
    +                                         <div
    +                                             className=\"dib\"
    +                                             include-no-scope=\"/templates/recipes/fragments/recipe-save-button.html\"
    +                                         />
    +                                         <div
    +                                             className=\"dib\"
    +                                             include-no-scope=\"/templates/recipes/fragments/recipe-tabs-other-links.html\"
    +                                         />
    +                                     </i>
    +                                 </div>
    +                                 <i className=\"icon icon-dku-nav_analysis\" />
    +                             </div>
    +                             <i className=\"icon icon-dku-nav_analysis\" />
    +                         </div>
    +                         <i className=\"icon icon-dku-nav_analysis\">
    +                             <div include-no-scope=\"/templates/recipes/fragments/recipe-summary-tab.html\">
    +                                 <div include-no-scope=\"/templates/recipes/fragments/recipe-io-tab.html\">
    +                                     <div include-no-scope=\"/templates/recipes/fragments/recipe-git-log.html\">
    +                                         {topNav.tab == 'settings' && computablesMap ? (
    +                                             <div className=\"dss-page\">
    +                                                 <div block-api-error=\"\">
    +                                                     <div className=\"h100 vertical-flex\">
    +                                                         <div className=\"flex\">
    +                                                             <div className=\"fh\">
    +                                                                 <div className=\"h100 oa\">
    +                                                                     <div className=\"row-fluid\">
    +                                                                         <div className=\"horizontal-centerer\">
    +                                                                             <div style=\"background-color: #ffffff\">
    +                                                                                 {recipeStatus.allMessagesForFrontend
    +                                                                                     .anyMessage ? (
    +                                                                                     <div style=\"padding: 10px\">
    +                                                                                         <div info-messages-raw-list-with-alert=\"recipeStatus.allMessagesForFrontend\" />
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                                 <div className=\"formbased-recipe-infozone w800\">
    +                                                                                     <ul>
    +                                                                                         <li>
    +                                                                                             {' '}
    +                                                                                             <strong>
    +                                                                                                 {
    +                                                                                                     desc.script.steps
    +                                                                                                         .length
    +                                                                                                 }
    +                                                                                             </strong>
    +                                                                                             准备步骤已经被应用
    +                                                                                         </li>
    +                                                                                         <li>
    +                                                                                             {' '}
    +                                                                                             <strong>
    +                                                                                                 {
    +                                                                                                     desc.modeling
    +                                                                                                         .algorithm
    +                                                                                                 }
    +                                                                                             </strong>
    +                                                                                             算法被使用
    +                                                                                         </li>
    +                                                                                     </ul>
    +                                                                                 </div>
    + 
    +                                                                                 <div className=\"recipe-settings-section1 w800\">
    +                                                                                     <h1 className=\"recipe-settings-section1-title\">
    +                                                                                         样本化
    +                                                                                     </h1>
    +                                                                                     <div className=\"recipe-settings-section2\">
    +                                                                                         {isMLBackendType(
    +                                                                                             'PY_MEMORY'
    +                                                                                         ) ? (
    +                                                                                             <p>
    +                                                                                                 如果您的数据集不适合您的内存，您可能需要对将要执行拆分的集合进行子采样
    +                                                                                             </p>
    +                                                                                         ) : null}
    +                                                                                         <form className=\"dkuform-horizontal\">
    +                                                                                             <div
    +                                                                                                 sampling-form-without-partitions=\"\"
    +                                                                                                 selection=\"desc.sampling.selection\"
    +                                                                                             />
    +                                                                                         </form>
    +                                                                                     </div>
    + 
    +                                                                                     {computablesMap ? (
    +                                                                                         <div className=\"recipe-settings-section1 w800\">
    +                                                                                             <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                 输出
    +                                                                                             </h1>
    +                                                                                             <div className=\"recipe-settings-section2\">
    +                                                                                                 <form className=\"dkuform-horizontal\">
    +                                                                                                     {preparedInputSchema ? (
    +                                                                                                         <div scoring-columns-filter=\"\" />
    +                                                                                                     ) : null}
    +                                                                                                 </form>
    +                                                                                             </div>
    + 
    +                                                                                             <div className=\"recipe-settings-section1 w800\">
    +                                                                                                 <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                     容器配置
    +                                                                                                 </h1>
    + 
    +                                                                                                 <div
    +                                                                                                     className=\"recipe-settings-section2\"
    +                                                                                                     container-selection-form=\"recipe.params.containerSelection\"
    +                                                                                                 />
    +                                                                                             </div>
    +                                                                                         </div>
    +                                                                                     ) : null}
    +                                                                                 </div>
    + 
    +                                                                                 {isMLBackendType('MLLIB') ? (
    +                                                                                     <div className=\"horizontal-centerer\">
    +                                                                                         <div className=\"recipe-settings-section1 w800\">
    +                                                                                             <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                 Spark 配置
    +                                                                                             </h1>
    +                                                                                             <div
    +                                                                                                 spark-override-config=\"\"
    +                                                                                                 config=\"desc.sparkParams.sparkConf\"
    +                                                                                                 task=\"desc\"
    +                                                                                                 task-type=\"MLLib\"
    +                                                                                                 className=\"recipe-settings-section2\"
    +                                                                                             />
    +                                                                                         </div>
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                             </div>
    +                                                                         </div>
    +                                                                     </div>
    +                                                                     {valCtx.preRunValidationError ||
    +                                                                     startedJob.jobId ? (
    +                                                                         <div className=\"noflex job-result-pane\">
    +                                                                             <div className=\"recipe-settings-floating-result\">
    +                                                                                 <div include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-result.html\" />
    +                                                                             </div>
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                 </div>
    + 
    +                                                                 <div className=\"recipe-settings-floating-run\">
    +                                                                     <div
    +                                                                         style=\"display:inline\"
    +                                                                         include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-partitions.html\"
    +                                                                     />
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/run-job-buttons.html\" />
    +                                                                 </div>
    +                                                             </div>
    + 
    +                                                             <div include-no-scope=\"/templates/recipes/fragments/limits-advanced-tab.html\" />
    +                                                         </div>
    +                                                     </div>
    +                                                 </div>
    +                                             </div>
    +                                         ) : null}
    +                                     </div>
    +                                 </div>
    +                             </div>
    +                         </i>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;