import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div ng-controller=\"CustomCodeRecipeController\" className=\"custom-code-recipe\">
    +             <div include-no-scope=\"/templates/recipes/fragments/simple-recipe-top-tabs.html\">
    +                 <div include-no-scope=\"/templates/recipes/fragments/recipe-summary-tab.html\">
    +                     <div include-no-scope=\"/templates/recipes/fragments/recipe-git-log.html\">
    +                         <div className=\"dss-page\">
    +                             <div block-api-error=\"\">
    +                                 {topNav.tab == 'settings' ? (
    +                                     <div className=\"h100\">
    +                                         <div className=\"h100 vertical-flex\">
    +                                             <div className=\"flex\">
    +                                                 <div className=\"fh\">
    +                                                     <div className=\"{'showLeftPane': desc.showInputsPane}\">
    +                                                         <div className=\"h100 leftPane oa\" style=\"background: white;\">
    +                                                             {/* lighter version of code-based-recipe-right-datasets-tab.html */}
    +                                                             {computablesMap ? (
    +                                                                 <div
    +                                                                     className=\"datasets datasets-explorer\"
    +                                                                     code-recipe-schema-list=\"\">
    +                                                                     <h4>输入数据集</h4>
    +                                                                     {recipe.inputs.length == 0 ? (
    +                                                                         <div className=\"nodatasets\">
    +                                                                             此组件没有输入数据集
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                     {recipe.inputs.map((inputs, index: number) => {
    +                                                                         return (
    +                                                                             <ul key={`item-${index}`}>
    +                                                                                 {inputs.items.map(
    +                                                                                     (input, index: number) => {
    +                                                                                         return (
    +                                                                                             <li
    +                                                                                                 key={`item-${index}`}
    +                                                                                                 foldable=\"false\"
    +                                                                                                 ng-init=\"computable =computablesMap[input.ref]\">
    +                                                                                                 {computable.type ==
    +                                                                                                 'MANAGED_FOLDER' ? (
    +                                                                                                     <div>
    +                                                                                                         <div className=\"pull-right actions\">
    +                                                                                                             <a
    +                                                                                                                 title=\"Open\"
    +                                                                                                                 href={`/projects/${
    +                                                                                                                     computable.projectKey
    +                                                                                                                 }/managedfolder/${
    +                                                                                                                     computable.name
    +                                                                                                                 }/view/`}>
    +                                                                                                                 <i className=\"icon-shaker\" />
    +                                                                                                             </a>
    +                                                                                                             <i className=\"icon-shaker\" />
    +                                                                                                         </div>
    +                                                                                                         <i className=\"icon-shaker\">
    +                                                                                                             <h5 style=\"cursor:inherit\">
    +                                                                                                                 {
    +                                                                                                                     computable.label
    +                                                                                                                 }
    +                                                                                                                 <span style=\"padding-left: 22px; font-weight: normal; color: #999\">
    +                                                                                                                     <br />(Folder:{' '}
    +                                                                                                                     {
    +                                                                                                                         computable.name
    +                                                                                                                     })
    +                                                                                                                 </span>
    +                                                                                                             </h5>
    +                                                                                                         </i>
    +                                                                                                     </div>
    +                                                                                                 ) : null}
    +                                                                                                 <i className=\"icon-shaker\">
    +                                                                                                     {computable.type ==
    +                                                                                                     'DATASET' ? (
    +                                                                                                         <div>
    +                                                                                                             <div className=\"pull-right actions\">
    +                                                                                                                 <a
    +                                                                                                                     title=\"Explore\"
    +                                                                                                                     href={`/projects/${
    +                                                                                                                         computable.projectKey
    +                                                                                                                     }/datasets/${
    +                                                                                                                         computable
    +                                                                                                                             .dataset
    +                                                                                                                             .name
    +                                                                                                                     }/explore/`}>
    +                                                                                                                     <i className=\"icon-shaker\" />
    +                                                                                                                 </a>
    +                                                                                                                 <i className=\"icon-shaker\" />
    +                                                                                                             </div>
    +                                                                                                             <i className=\"icon-shaker\">
    +                                                                                                                 <h5
    +                                                                                                                     onClick={() => {
    +                                                                                                                         toggleFold();
    +                                                                                                                     }}>
    +                                                                                                                     <i
    +                                                                                                                         className={`icon-fixed-width icon-${
    +                                                                                                                             unfolded
    +                                                                                                                                 ? 'caret-down'
    +                                                                                                                                 : 'caret-right'
    +                                                                                                                         }`}>
    +                                                                                                                         &nbsp;{
    +                                                                                                                             input.ref
    +                                                                                                                         }
    +                                                                                                                         {computable
    +                                                                                                                             .dataset
    +                                                                                                                             .params
    +                                                                                                                             .table ? (
    +                                                                                                                             <span style=\"padding-left: 22px; font-weight: normal; color: #999\">
    +                                                                                                                                 <br />表:{' '}
    +                                                                                                                                 {
    +                                                                                                                                     computable
    +                                                                                                                                         .dataset
    +                                                                                                                                         .params
    +                                                                                                                                         .table
    +                                                                                                                                 }
    +                                                                                                                             </span>
    +                                                                                                                         ) : null}
    +                                                                                                                     </i>
    +                                                                                                                 </h5>
    +                                                                                                                 <i
    +                                                                                                                     className={`icon-fixed-width icon-${
    +                                                                                                                         unfolded
    +                                                                                                                             ? 'caret-down'
    +                                                                                                                             : 'caret-right'
    +                                                                                                                     }`}>
    +                                                                                                                     {unfolded &&
    +                                                                                                                     !computable
    +                                                                                                                         .dataset
    +                                                                                                                         .schema
    +                                                                                                                         .columns
    +                                                                                                                         .length ? (
    +                                                                                                                         <div className=\"noschema\">
    +                                                                                                                             此数据集没有模式
    +                                                                                                                         </div>
    +                                                                                                                     ) : null}
    +                                                                                                                     {unfolded ? (
    +                                                                                                                         <ul className=\"schema\">
    +                                                                                                                             {computable.dataset.schema.columns.map(
    +                                                                                                                                 (
    +                                                                                                                                     col,
    +                                                                                                                                     index: number
    +                                                                                                                                 ) => {
    +                                                                                                                                     return (
    +                                                                                                                                         <li
    +                                                                                                                                             key={`item-${index}`}
    +                                                                                                                                             className=\"horizontal-flex\">
    +                                                                                                                                             <a
    +                                                                                                                                                 main-click=\"\"
    +                                                                                                                                                 className=\"flex mx-textellipsis\">
    +                                                                                                                                                 {
    +                                                                                                                                                     col.name
    +                                                                                                                                                 }
    +                                                                                                                                             </a>
    + 
    +                                                                                                                                             <span className=\"type noflex text-right\">
    +                                                                                                                                                 {
    +                                                                                                                                                     col.type
    +                                                                                                                                                 }
    +                                                                                                                                             </span>
    +                                                                                                                                         </li>
    +                                                                                                                                     );
    +                                                                                                                                 }
    +                                                                                                                             )}
    +                                                                                                                         </ul>
    +                                                                                                                     ) : null}
    +                                                                                                                 </i>
    +                                                                                                             </i>
    +                                                                                                         </div>
    +                                                                                                     ) : null}
    +                                                                                                     <i className=\"icon-shaker\">
    +                                                                                                         <i
    +                                                                                                             className={`icon-fixed-width icon-${
    +                                                                                                                 unfolded
    +                                                                                                                     ? 'caret-down'
    +                                                                                                                     : 'caret-right'
    +                                                                                                             }`}
    +                                                                                                         />
    +                                                                                                     </i>
    +                                                                                                 </i>
    +                                                                                             </li>
    +                                                                                         );
    +                                                                                     }
    +                                                                                 )}
    +                                                                                 <i className=\"icon-shaker\">
    +                                                                                     <i className=\"icon-shaker\">
    +                                                                                         <i
    +                                                                                             className={`icon-fixed-width icon-${
    +                                                                                                 unfolded
    +                                                                                                     ? 'caret-down'
    +                                                                                                     : 'caret-right'
    +                                                                                             }`}
    +                                                                                         />
    +                                                                                     </i>
    +                                                                                 </i>
    +                                                                             </ul>
    +                                                                         );
    +                                                                     })}
    +                                                                     <i className=\"icon-shaker\">
    +                                                                         <i className=\"icon-shaker\">
    +                                                                             <i
    +                                                                                 className={`icon-fixed-width icon-${
    +                                                                                     unfolded
    +                                                                                         ? 'caret-down'
    +                                                                                         : 'caret-right'
    +                                                                                 }`}
    +                                                                             />
    +                                                                         </i>
    +                                                                     </i>
    +                                                                 </div>
    +                                                             ) : null}
    +                                                             <i className=\"icon-shaker\">
    +                                                                 <i className=\"icon-shaker\">
    +                                                                     <i
    +                                                                         className={`icon-fixed-width icon-${
    +                                                                             unfolded ? 'caret-down' : 'caret-right'
    +                                                                         }`}
    +                                                                     />
    +                                                                 </i>
    +                                                             </i>
    +                                                         </div>
    +                                                         <i className=\"icon-shaker\">
    +                                                             <i className=\"icon-shaker\">
    +                                                                 <i
    +                                                                     className={`icon-fixed-width icon-${
    +                                                                         unfolded ? 'caret-down' : 'caret-right'
    +                                                                     }`}>
    +                                                                     <div className=\"h100 mainPane\">
    +                                                                         <div
    +                                                                             className=\"fh w800 oa\"
    +                                                                             style=\"margin: auto;\">
    +                                                                             <div
    +                                                                                 className=\" \"
    +                                                                                 style=\"margin-top: 40px; margin-bottom: 20px\">
    +                                                                                 <h1 className=\"custom-code-recipe-title\">
    +                                                                                     {desc.meta.label || desc.id}
    +                                                                                 </h1>
    +                                                                                 <div className=\"recipe-settings-section2\">
    +                                                                                     <div from-markdown=\"desc.meta.description\">
    +                                                                                         {pluginDesc.url ? (
    +                                                                                             <a
    +                                                                                                 href={plugin.url}
    +                                                                                                 target=\"_blank\"
    +                                                                                                 rel=\"noopener noreferrer\">
    +                                                                                                 学习更多补丁 &nbsp;<i className=\"icon-external-link\" />
    +                                                                                             </a>
    +                                                                                         ) : null}
    +                                                                                     </div>
    +                                                                                 </div>
    + 
    +                                                                                 <div
    +                                                                                     className=\"recipe-settings-section1 \"
    +                                                                                     style=\"margin-top: 40px; margin-bottom: 20px\">
    +                                                                                     <h1 className=\"recipe-settings-section1-title\">
    +                                                                                         组件设置
    +                                                                                     </h1>
    +                                                                                     <div
    +                                                                                         plugin-settings-alert=\"\"
    +                                                                                         component-type=\"recipe\"
    +                                                                                         has-settings=\"pluginDesc.hasSettings\"
    +                                                                                         app-config=\"appConfig\">
    +                                                                                         <div
    +                                                                                             custom-params-form=\"\"
    +                                                                                             desc=\"desc\"
    +                                                                                             plugin-desc=\"pluginDesc\"
    +                                                                                             component-id=\"loadedDesc.id\"
    +                                                                                             config=\"recipe.params.customConfig\"
    +                                                                                             columns-per-input-role=\"columnsPerInputRole\"
    +                                                                                             recipe-config=\"recipe\"
    +                                                                                         />
    +                                                                                     </div>
    +                                                                                 </div>
    +                                                                             </div>
    +                                                                         </div>
    +                                                                     </div>
    +                                                                     {valCtx.preRunValidationError ||
    +                                                                     startedJob.jobId ? (
    +                                                                         <div className=\"noflex\">
    +                                                                             <div className=\"recipe-settings-floating-result recipe-job-result\">
    +                                                                                 <div include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-result.html\" />
    +                                                                             </div>
    +                                                                         </div>
    +                                                                     ) : null}
    +                                                                 </i>
    +                                                             </i>
    +                                                         </i>
    +                                                     </div>
    +                                                     <i className=\"icon-shaker\">
    +                                                         <i className=\"icon-shaker\">
    +                                                             <i
    +                                                                 className={`icon-fixed-width icon-${
    +                                                                     unfolded ? 'caret-down' : 'caret-right'
    +                                                                 }`}>
    +                                                                 <div className=\"recipe-settings-floating-run\">
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/recipe-editor-job-partitions.html\" />
    +                                                                     <div include-no-scope=\"/templates/recipes/fragments/run-job-buttons.html\" />
    +                                                                 </div>
    +                                                             </i>
    +                                                         </i>
    +                                                     </i>
    +                                                 </div>
    +                                                 <i className=\"icon-shaker\">
    +                                                     <i className=\"icon-shaker\">
    +                                                         <i
    +                                                             className={`icon-fixed-width icon-${
    +                                                                 unfolded ? 'caret-down' : 'caret-right'
    +                                                             }`}
    +                                                         />
    +                                                     </i>
    +                                                 </i>
    +                                             </div>
    +                                             <i className=\"icon-shaker\">
    +                                                 <i className=\"icon-shaker\">
    +                                                     <i
    +                                                         className={`icon-fixed-width icon-${
    +                                                             unfolded ? 'caret-down' : 'caret-right'
    +                                                         }`}>
    +                                                         {topNav.tab == 'io' ? (
    +                                                             <div className=\"container-fluid h100 oa\">
    +                                                                 <div
    +                                                                     className=\"h100\"
    +                                                                     include-no-scope=\"/templates/recipes/code-based/custom-code-recipe-io.html\"
    +                                                                 />
    + 
    +                                                                 {topNav.tab == 'advanced' ? (
    +                                                                     <div className=\"dss-page small-lr-padding\">
    +                                                                         <div block-api-error=\"\">
    +                                                                             <div className=\"horizontal-centerer\">
    +                                                                                 {desc.kind == 'PYSPARK' ||
    +                                                                                 desc.kind == 'SPARK_SCALA' ? (
    +                                                                                     <div>
    +                                                                                         <div className=\"recipe-settings-section1 w800\">
    +                                                                                             <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                 Spark 配置
    +                                                                                             </h1>
    +                                                                                             <div
    +                                                                                                 spark-override-config=\"\"
    +                                                                                                 config=\"recipe.params.sparkConfig\"
    +                                                                                                 className=\"recipe-settings-section2\">
    +                                                                                                 {desc.codeMode ==
    +                                                                                                 'FUNCTION' ? (
    +                                                                                                     <form className=\"dkuform-horizontal recipe-settings-section2\">
    +                                                                                                         <h5>
    +                                                                                                             Spark 处理
    +                                                                                                             &amp;
    +                                                                                                             Metastore
    +                                                                                                         </h5>
    +                                                                                                         <div className=\"control-group\">
    +                                                                                                             <label className=\"control-label\">
    +                                                                                                                 Spark
    +                                                                                                                 处理
    +                                                                                                             </label>
    +                                                                                                             <div className=\"controls\">
    +                                                                                                                 <label>
    +                                                                                                                     <input
    +                                                                                                                         type=\"checkbox\"
    +                                                                                                                         value={
    +                                                                                                                             recipe
    +                                                                                                                                 .params
    +                                                                                                                                 .pipelineAllowMerge
    +                                                                                                                         }
    +                                                                                                                     />
    +                                                                                                                     <span className=\"help-inline\">
    +                                                                                                                         可以将此组件合并到现有的Spark组件处理中?
    +                                                                                                                     </span>
    +                                                                                                                 </label>
    +                                                                                                                 <label>
    +                                                                                                                     <input
    +                                                                                                                         type=\"checkbox\"
    +                                                                                                                         value={
    +                                                                                                                             recipe
    +                                                                                                                                 .params
    +                                                                                                                                 .pipelineAllowStart
    +                                                                                                                         }
    +                                                                                                                     />
    +                                                                                                                     <span className=\"help-inline\">
    +                                                                                                                         这个组件可以成为Spark组件处理的目标?
    +                                                                                                                     </span>
    +                                                                                                                 </label>
    +                                                                                                             </div>
    +                                                                                                         </div>
    +                                                                                                         <div className=\"control-group\">
    +                                                                                                             <label className=\"control-label\">
    +                                                                                                                 Hive
    +                                                                                                                 metastore
    +                                                                                                             </label>
    +                                                                                                             <div className=\"controls\">
    +                                                                                                                 <label>
    +                                                                                                                     <input
    +                                                                                                                         type=\"checkbox\"
    +                                                                                                                         value={
    +                                                                                                                             recipe
    +                                                                                                                                 .params
    +                                                                                                                                 .useGlobalMetastore
    +                                                                                                                         }
    +                                                                                                                     />
    +                                                                                                                     <span className=\"help-inline\">
    +                                                                                                                         使用全局的
    +                                                                                                                         Hive
    +                                                                                                                         metastore
    +                                                                                                                     </span>
    +                                                                                                                 </label>
    +                                                                                                             </div>
    +                                                                                                         </div>
    +                                                                                                     </form>
    +                                                                                                 ) : null}
    +                                                                                             </div>
    +                                                                                         </div>
    +                                                                                         {desc.kind == 'PYTHON' ||
    +                                                                                         desc.kind == 'R' ? (
    +                                                                                             <div>
    +                                                                                                 <div className=\"recipe-settings-section1 w800\">
    +                                                                                                     <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                         容器配置
    +                                                                                                     </h1>
    + 
    +                                                                                                     <div
    +                                                                                                         className=\"recipe-settings-section2\"
    +                                                                                                         container-selection-form=\"recipe.params.containerSelection\"
    +                                                                                                     />
    +                                                                                                 </div>
    +                                                                                             </div>
    +                                                                                         ) : null}
    +                                                                                         <div className=\"recipe-settings-section1 w800\">
    +                                                                                             <h1 className=\"recipe-settings-section1-title\">
    +                                                                                                 并发活动
    +                                                                                             </h1>
    +                                                                                             <form className=\"dkuform-horizontal recipe-settings-section2\">
    +                                                                                                 <div className=\"control-group\">
    +                                                                                                     <label className=\"control-label\">
    +                                                                                                         组件限制
    +                                                                                                     </label>
    +                                                                                                     <div className=\"controls\">
    +                                                                                                         <input
    +                                                                                                             type=\"number\"
    +                                                                                                             value={
    +                                                                                                                 recipe.maxRunningActivities
    +                                                                                                             }
    +                                                                                                         />
    +                                                                                                         <span className=\"help-inline\">
    +                                                                                                             限制此配方的并发活动数
    +                                                                                                             (0为
    +                                                                                                             \"无限\").
    +                                                                                                         </span>
    +                                                                                                     </div>
    +                                                                                                 </div>
    +                                                                                             </form>
    +                                                                                         </div>
    +                                                                                     </div>
    +                                                                                 ) : null}
    +                                                                             </div>
    +                                                                         </div>
    +                                                                     </div>
    +                                                                 ) : null}
    +                                                             </div>
    +                                                         ) : null}
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </div>
    +                                     </div>
    +                                 ) : null}
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