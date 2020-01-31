import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\" ng-controller=\"NewNotebookModalController\">
    +             <DkuModalHeader modal-class=\"has-border\">
    +                 <DkuModalTitle>
    +                     {uiState.step == 'choose-type' ? <span>新建脚本</span> : null}
    +                     {uiState.step == 'sql' ? <span>新建SQL脚本</span> : null}
    +                     {uiState.step == 'impala' ? <span>新建Impala脚本</span> : null}
    +                     {uiState.step == 'hive' ? <span>新建Hive脚本</span> : null}
    +                     {uiState.step == 'python' ? <span>新建Python脚本</span> : null}
    +                     {uiState.step == 'r' ? <span>新建R脚本</span> : null}
    +                     {uiState.step == 'customjupyter' ? <span>新建Jupyter脚本</span> : null}
    +                     {uiState.step == 'scala' ? <span>新建Scala脚本</span> : null}
    +                 </DkuModalTitle>
    +             </DkuModalHeader>
    + 
    +             {uiState.step == 'choose-type' ? (
    +                 <div className=\"modal-body\">
    +                     <div style=\"display: flex; flex-direction: row; flex-wrap: wrap;  align-items: center; justify-content: space-around; align-content: space-around; height: 100%;\">
    +                         <div
    +                             onClick={() => {
    +                                 uiState.step = 'sql';
    +                             }}
    +                             className=\"new-notebook-modal-pic selectable\">
    +                             <i className=\"icon-code_sql_recipe icon-3x\">
    +                                 <h4>SQL</h4>
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-code_sql_recipe icon-3x\">
    +                             {/* Hive */}
    +                             {/* somewhat ugly trick to disable the button: condition the ng-click itself */}
    +                             <div
    +                                 onClick={() => {
    +                                     appConfig.hiveEnabled && (uiState.step = 'hive');
    +                                 }}
    +                                 className=\"{'selectable' : appConfig.hiveEnabled}\">
    +                                 <i className=\"icon-code_hive_recipe icon-3x\">
    +                                     <h4>Hive</h4>
    +                                 </i>
    +                             </div>
    +                             <i className=\"icon-code_hive_recipe icon-3x\">
    +                                 {/* Impala */}
    +                                 <div
    +                                     onClick={() => {
    +                                         appConfig.impalaEnabled && (uiState.step = 'impala');
    +                                     }}
    +                                     className=\"{'selectable' : appConfig.impalaEnabled}\"
    +                                     disabled={!appConfig.impalaEnabled}>
    +                                     <i className=\"icon-code_impala_recipe icon-3x\">
    +                                         <h4>Impala</h4>
    +                                     </i>
    +                                 </div>
    +                                 <i className=\"icon-code_impala_recipe icon-3x\">
    +                                     <div
    +                                         onClick={() => {
    +                                             uiState.step = 'python';
    +                                         }}
    +                                         className=\"new-notebook-modal-pic selectable\">
    +                                         <i className=\"icon-code_python_recipe icon-3x\">
    +                                             <h4>Python</h4>
    +                                         </i>
    +                                     </div>
    +                                     <i className=\"icon-code_python_recipe icon-3x\">
    +                                         <div
    +                                             onClick={() => {
    +                                                 uiState.step = 'r';
    +                                             }}
    +                                             className=\"new-notebook-modal-pic selectable\">
    +                                             <i className=\"icon-code_r_recipe icon-3x\">
    +                                                 <h4>R</h4>
    +                                             </i>
    +                                         </div>
    +                                         <i className=\"icon-code_r_recipe icon-3x\">
    +                                             <div
    +                                                 onClick={() => {
    +                                                     appConfig.sparkEnabled && (uiState.step = 'scala');
    +                                                 }}
    +                                                 className=\"{'selectable' : appConfig.sparkEnabled}\"
    +                                                 disabled={!appConfig.sparkEnabled}>
    +                                                 <i className=\"icon-code_spark_scala_recipe icon-3x\">
    +                                                     <h4>Scala (Spark)</h4>
    +                                                 </i>
    +                                             </div>
    +                                             <i className=\"icon-code_spark_scala_recipe icon-3x\" />
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-code_sql_recipe icon-3x\">
    +                         <i className=\"icon-code_hive_recipe icon-3x\">
    +                             <i className=\"icon-code_impala_recipe icon-3x\">
    +                                 <i className=\"icon-code_python_recipe icon-3x\">
    +                                     <i className=\"icon-code_r_recipe icon-3x\">
    +                                         <i className=\"icon-code_spark_scala_recipe icon-3x\" />
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </div>
    +             ) : null}
    +             <i className=\"icon-code_sql_recipe icon-3x\">
    +                 <i className=\"icon-code_hive_recipe icon-3x\">
    +                     <i className=\"icon-code_impala_recipe icon-3x\">
    +                         <i className=\"icon-code_python_recipe icon-3x\">
    +                             <i className=\"icon-code_r_recipe icon-3x\">
    +                                 <i className=\"icon-code_spark_scala_recipe icon-3x\">
    +                                     {uiState.step == 'sql' || uiState.step == 'impala' || uiState.step == 'hive' ? (
    +                                         <form
    +                                             className=\"dkuform-modal-horizontal dkuform-modal-wrapper\"
    +                                             name=\"newSQLNotebookForm\">
    +                                             <div className=\"modal-body\">
    +                                                 <div block-api-error=\"\">
    +                                                     <div className=\"control-group\">
    +                                                         <label className=\"control-label\">连接</label>
    +                                                         <div className=\"controls\">
    +                                                             {uiState.step == 'impala' ? (
    +                                                                 <select
    +                                                                     dku-bs-select=\"{liveSearch:true}\"
    +                                                                     value={newNotebook.connection}
    +                                                                     ng-options=\"connection.name as connection.label group by connection.type for connection in connections | filter:{ type : 'Impala' }\"
    +                                                                 />
    +                                                             ) : null}
    +                                                             {uiState.step == 'hive' ? (
    +                                                                 <select
    +                                                                     dku-bs-select=\"{liveSearch:true}\"
    +                                                                     value={newNotebook.connection}
    +                                                                     ng-options=\"connection.name as connection.label group by connection.type for connection in connections | filter:{ type : 'Hive' }\"
    +                                                                 />
    +                                                             ) : null}
    +                                                             {uiState.step != 'hive' && uiState.step != 'impala' ? (
    +                                                                 <select
    +                                                                     dku-bs-select=\"{liveSearch:true}\"
    +                                                                     value={newNotebook.connection}
    +                                                                     ng-options=\"connection.name as connection.label group by connection.type for connection in connections | filter:{ type : '!Hive' } | filter:{ type : '!Impala' }\"
    +                                                                 />
    +                                                             ) : null}
    +                                                         </div>
    +                                                     </div>
    + 
    +                                                     {hiveError && ['hive', 'impala'].indexOf(uiState.step) >= 0 ? (
    +                                                         <div
    +                                                             className=\"alert alert-warning\"
    +                                                             style=\"margin: 10px 0 0 0\"
    +                                                             ng-init=\"showHiveError=false;\">
    +                                                             不能列出 Hive 数据库, 发生一个错误
    +                                                             <i
    +                                                                 className=\"{'icon-eye-open': !showHiveError, 'icon-eye-close': showHiveError}\"
    +                                                                 onClick={() => {
    +                                                                     showHiveError = !showHiveError;
    +                                                                 }}
    +                                                             />
    +                                                             {showHiveError ? (
    +                                                                 <div
    +                                                                     api-error-alert=\"hiveError\"
    +                                                                     closable=\"false\"
    +                                                                     error-foldable=\"false\"
    +                                                                 />
    +                                                             ) : null}
    +                                                             <div className=\"control-group\">
    +                                                                 <label className=\"control-label\">Notebook 名称</label>
    +                                                                 <div className=\"controls\">
    +                                                                     <input
    +                                                                         name=\"datasetName\"
    +                                                                         value={newNotebook.name}
    +                                                                         type=\"text\"
    +                                                                     />
    +                                                                 </div>
    +                                                             </div>
    +                                                         </div>
    +                                                     ) : null}
    +                                                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                         <button
    +                                                             className=\"btn btn-primary\"
    +                                                             disabled={!newNotebook.connection || !newNotebook.name}
    +                                                             onClick={() => {
    +                                                                 createAndRedirect();
    +                                                             }}>
    +                                                             &nbsp;创建
    +                                                         </button>
    +                                                     </div>
    + 
    +                                                     {uiState.step == 'python' ||
    +                                                     uiState.step == 'r' ||
    +                                                     uiState.step == 'scala' ? (
    +                                                         <form
    +                                                             className=\"dkuform-modal-horizontal dkuform-modal-wrapper\"
    +                                                             name=\"newJupyterNotebookForm\">
    +                                                             <div className=\"modal-body\">
    +                                                                 <div block-api-error=\"\">
    +                                                                     <ul className=\"new-notebook-modal-templates\">
    +                                                                         {availableTemplates.map(
    +                                                                             (template, index: number) => {
    +                                                                                 return (
    +                                                                                     <li
    +                                                                                         key={`item-${index}`}
    +                                                                                         className=\"{selected: newNotebook.template.id == template.id}\"
    +                                                                                         onClick={() => {
    +                                                                                             newNotebook.template = template;
    +                                                                                         }}>
    +                                                                                         <span className=\"selection-indicator\">
    +                                                                                             {newNotebook.template.id ==
    +                                                                                             template.id ? (
    +                                                                                                 <i
    +                                                                                                     className=\"icon-ok\"
    +                                                                                                     title=\"active\"
    +                                                                                                 />
    +                                                                                             ) : null}
    +                                                                                         </span>
    +                                                                                         {newNotebook.template.id ==
    +                                                                                         template.id ? (
    +                                                                                             <i
    +                                                                                                 className=\"icon-ok\"
    +                                                                                                 title=\"active\">
    +                                                                                                 <span
    +                                                                                                     className={`name qa-notebook-template--${
    +                                                                                                         template.id
    +                                                                                                     }`}>
    +                                                                                                     模板:{
    +                                                                                                         template.label
    +                                                                                                     }
    +                                                                                                 </span>
    +                                                                                             </i>
    +                                                                                         ) : null}
    +                                                                                     </li>
    +                                                                                 );
    +                                                                             }
    +                                                                         )}
    +                                                                         {newNotebook.template.id == template.id ? (
    +                                                                             <i className=\"icon-ok\" title=\"active\" />
    +                                                                         ) : null}
    +                                                                     </ul>
    +                                                                     {newNotebook.template.id == template.id ? (
    +                                                                         <i className=\"icon-ok\" title=\"active\">
    +                                                                             {newNotebook.language == 'python' ||
    +                                                                             newNotebook.language == 'r' ? (
    +                                                                                 <div className=\"control-group\">
    +                                                                                     <label
    +                                                                                         className=\"control-label\"
    +                                                                                         htmlFor=\"notebookEnv\">
    +                                                                                         代码环境
    +                                                                                     </label>
    +                                                                                     <div className=\"controls\">
    +                                                                                         <select
    +                                                                                             dku-bs-select=\"\"
    +                                                                                             value={newNotebook.codeEnv}
    +                                                                                             ng-options=\"codeEnv[0] as codeEnv[1] for codeEnv in availableCodeEnvs\">
    +                                                                                             Notebook 名称
    +                                                                                         </select>
    +                                                                                         <input
    +                                                                                             name=\"datasetName\"
    +                                                                                             value={newNotebook.name}
    +                                                                                             type=\"text\"
    +                                                                                             pattern=\"/^[^#.*/\\\\]+$/\"
    +                                                                                             required={true}
    +                                                                                         />
    +                                                                                         {newJupyterNotebookForm
    +                                                                                             .datasetName.$error
    +                                                                                             .pattern ? (
    +                                                                                             <span className=\"help-inline\">
    +                                                                                                 不包含{' '}
    +                                                                                                 <code>#.*/\\</code>
    +                                                                                             </span>
    +                                                                                         ) : null}
    +                                                                                     </div>
    +                                                                                 </div>
    +                                                                             ) : null}
    +                                                                         </i>
    +                                                                     ) : null}
    +                                                                 </div>
    +                                                                 {newNotebook.template.id == template.id ? (
    +                                                                     <i className=\"icon-ok\" title=\"active\">
    +                                                                         <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                                             <button
    +                                                                                 className=\"btn btn-primary\"
    +                                                                                 disabled={
    +                                                                                     newJupyterNotebookForm.$invalid ||
    +                                                                                     !newNotebook.name
    +                                                                                 }
    +                                                                                 onClick={() => {
    +                                                                                     createAndRedirect();
    +                                                                                 }}>
    +                                                                                 &nbsp;创建
    +                                                                             </button>
    +                                                                         </div>
    +                                                                     </i>
    +                                                                 ) : null}
    +                                                             </div>
    +                                                         </form>
    +                                                     ) : null}
    +                                                 </div>
    +                                             </div>
    +                                         </form>
    +                                     ) : null}
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </i>
    +             </i>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;