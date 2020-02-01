import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div ng-controller=\"ClusteringScoringRecipeEditor\">
    +             <div className=\"top-level-tabs objecttype-recipe\">
    +                 <div className=\"row-fluid object-nav horizontal-flex\">
    +                     <div std-object-breadcrumb=\"\" className=\"flex oh\">
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
    +                                 className=\"{'tab': true, 'enabled': topNav.tab == 'advanced'}\"
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
    +                                         <div include-no-scope=\"/templates/recipes/fragments/limits-advanced-tab.html\">
    +                                             {topNav.tab == 'settings' ? (
    +                                                 <div className=\"dss-page summary-tab\" style=\"margin-top: 10px;\">
    +                                                     <div block-api-error=\"\">
    +                                                         <div className=\"h100 vertical-flex\">
    +                                                             <div className=\"flex\">
    +                                                                 <div className=\"fh\">
    +                                                                     <div className=\"h100 oa\">
    +                                                                         <div style=\"background-color: #ffffff\">
    +                                                                             {recipeStatus.allMessagesForFrontend
    +                                                                                 .anyMessage ? (
    +                                                                                 <div style=\"padding: 10px\">
    +                                                                                     <div info-messages-raw-list-with-alert=\"recipeStatus.allMessagesForFrontend\" />
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                             {computablesMap ? (
    +                                                                                 <div className=\"recipe-settings-section1 w800\">
    +                                                                                     <h1 className=\"recipe-settings-section1-title\">
    +                                                                                         输出
    +                                                                                     </h1>
    +                                                                                     <div className=\"recipe-settings-section2\">
    +                                                                                         <form className=\"dkuform-horizontal\">
    +                                                                                             {preparedInputSchema ? (
    +                                                                                                 <div scoring-columns-filter=\"\" />
    +                                                                                             ) : null}
    +                                                                                         </form>
    +                                                                                     </div>
    + 
    +                                                                                     {isMLBackendType('MLLIB') ? (
    +                                                                                         <div className=\"horizontal-centerer\">
    +                                                                                             <div className=\"recipe-settings-section1 w800\">
    +                                                                                                 <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                     Spark 配置
    +                                                                                                 </h1>
    +                                                                                                 <div
    +                                                                                                     spark-override-config=\"\"
    +                                                                                                     config=\"desc.sparkParams.sparkConf\"
    +                                                                                                     task=\"desc\"
    +                                                                                                     task-type=\"MLLib\"
    +                                                                                                     className=\"recipe-settings-section2\"
    +                                                                                                 />
    +                                                                                             </div>
    +                                                                                         </div>
    +                                                                                     ) : null}
    + 
    +                                                                                     <div className=\"recipe-settings-section1 w800\">
    +                                                                                         <h1 className=\"recipe-settings-section1-title\">
    +                                                                                             容器配置
    +                                                                                         </h1>
    + 
    +                                                                                         <div
    +                                                                                             className=\"recipe-settings-section2\"
    +                                                                                             container-selection-form=\"recipe.params.containerSelection\"
    +                                                                                         />
    +                                                                                     </div>
    +                                                                                 </div>
    +                                                                             ) : null}
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
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-partitions.html\" />
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/run-job-buttons.html\" />
    +                                                                 </div>
    +                                                             </div>
    +                                                         </div>
    +                                                     </div>
    +                                                 </div>
    +                                             ) : null}
    +                                         </div>
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