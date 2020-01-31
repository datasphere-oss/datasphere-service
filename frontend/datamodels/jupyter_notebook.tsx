import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div navigator-object=\"\">
    +             <div className=\"object-nav horizontal-flex\">
    +                 <div std-object-breadcrumb=\"\" className=\"flex oh\">
    +                     <div className=\"noflex\">
    +                         <div className=\"otherLinks\">
    +                             <div discussions-button=\"\">
    +                                 {snippetsType != null ? (
    +                                     <div
    +                                         className=\"dib\"
    +                                         code-snippet-editor-switch=\"\"
    +                                         code-samples-selector-visible=\"uiState.codeSamplesSelectorVisible\"
    +                                     />
    +                                 ) : null}
    +                                 {isProjectAnalystRW() && notebook.content.metadata.associatedRecipe.length ? (
    +                                     <button
    +                                         className=\"btn btn-action\"
    +                                         onClick={() => {
    +                                             saveBackToRecipe(notebook);
    +                                         }}
    +                                         title={`This notebook is associated to recipe '${
    +                                             notebook.content.metadata.associatedRecipe
    +                                         }'`}>
    +                                         <i className=\"icon-save\"> 保存返回到组件</i>
    +                                     </button>
    +                                 ) : null}
    +                                 <i className=\"icon-save\">
    +                                     {isProjectAnalystRW() && !notebook.content.metadata.associatedRecipe.length ? (
    +                                         <button
    +                                             className=\"btn btn-action\"
    +                                             onClick={() => {
    +                                                 createRecipeFromNotebook(notebook);
    +                                             }}>
    +                                             <i className=\"icon-plus\"> 创建组件</i>
    +                                         </button>
    +                                     ) : null}
    +                                     <i className=\"icon-plus\">
    +                                         {isProjectAnalystRW() || canWriteDashboards() ? (
    +                                             <div className=\"dropdown\">
    +                                                 <button
    +                                                     className=\"btn btn-action dropdown-toggle\"
    +                                                     data-toggle=\"dropdown\"
    +                                                     href=\"\">
    +                                                     操作 <i className=\"icon-caret-down\" />
    +                                                 </button>
    +                                                 <i className=\"icon-caret-down\">
    +                                                     <ul className=\"dropdown-menu pull-right text-left\">
    +                                                         {canWriteDashboards() ? (
    +                                                             <li>
    +                                                                 <a
    +                                                                     onClick={() => {
    +                                                                         exportCreateAndPinInsight(notebook);
    +                                                                     }}>
    +                                                                     <i className=\"icon-dku-publish\"> 发布</i>
    +                                                                 </a>
    +                                                                 <i className=\"icon-dku-publish\" />
    +                                                             </li>
    +                                                         ) : null}
    +                                                         <i className=\"icon-dku-publish\">
    +                                                             {projectSummary.canManageExposedElements ? (
    +                                                                 <li>
    +                                                                     <a
    +                                                                         ng-inject=\"ExposedObjectsService\"
    +                                                                         onClick={() => {
    +                                                                             ExposedObjectsService.exposeSingleObject(
    +                                                                                 'JUPYTER_NOTEBOOK',
    +                                                                                 notebook.name
    +                                                                             );
    +                                                                         }}>
    +                                                                         <i className=\"icon-dku-share\"> 共享</i>
    +                                                                     </a>
    +                                                                     <i className=\"icon-dku-share\" />
    +                                                                 </li>
    +                                                             ) : null}
    +                                                             <i className=\"icon-dku-share\">
    +                                                                 {isProjectAnalystRW() ? (
    +                                                                     <li>
    +                                                                         <a
    +                                                                             onClick={() => {
    +                                                                                 deleteNotebook(notebook);
    +                                                                             }}
    +                                                                             className=\"text-error\">
    +                                                                             <span className=\"text-error\">
    +                                                                                 <i className=\"icon-trash\"> 删除</i>
    +                                                                             </span>
    +                                                                             <i className=\"icon-trash\" />
    +                                                                         </a>
    +                                                                         <i className=\"icon-trash\" />
    +                                                                     </li>
    +                                                                 ) : null}
    +                                                                 <i className=\"icon-trash\" />
    +                                                             </i>
    +                                                         </i>
    +                                                     </ul>
    +                                                     <i className=\"icon-dku-publish\">
    +                                                         <i className=\"icon-dku-share\">
    +                                                             <i className=\"icon-trash\" />
    +                                                         </i>
    +                                                     </i>
    +                                                 </i>
    +                                             </div>
    +                                         ) : null}
    +                                         <i className=\"icon-caret-down\">
    +                                             <i className=\"icon-dku-publish\">
    +                                                 <i className=\"icon-dku-share\">
    +                                                     <i className=\"icon-trash\" />
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </div>
    +                             <i className=\"icon-save\">
    +                                 <i className=\"icon-plus\">
    +                                     <i className=\"icon-caret-down\">
    +                                         <i className=\"icon-dku-publish\">
    +                                             <i className=\"icon-dku-share\">
    +                                                 <i className=\"icon-trash\" />
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </div>
    +                         <i className=\"icon-save\">
    +                             <i className=\"icon-plus\">
    +                                 <i className=\"icon-caret-down\">
    +                                     <i className=\"icon-dku-publish\">
    +                                         <i className=\"icon-dku-share\">
    +                                             <i className=\"icon-trash\">
    +                                                 {snippetsType != null ? (
    +                                                     <CodeSnippetSampleSelector
    +                                                         style=\"min-height: 400px;width: 100%; position: absolute; top: 100%;\"
    +                                                         insert-code-func=\"copySnippetToClipboard_\"
    +                                                         code-samples-selector-visible=\"uiState.codeSamplesSelectorVisible\"
    +                                                         insert-button-label=\"'COPY TO CLIPBOARD'\"
    +                                                         sample-type=\"snippetsType\"
    +                                                         categories=\"snippetsCategories\"
    +                                                         save-category=\"snippetsSaveCategory\"
    +                                                     />
    +                                                 ) : null}
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </div>
    +                     <i className=\"icon-save\">
    +                         <i className=\"icon-plus\">
    +                             <i className=\"icon-caret-down\">
    +                                 <i className=\"icon-dku-publish\">
    +                                     <i className=\"icon-dku-share\">
    +                                         <i className=\"icon-trash\">
    +                                             <DkuFrame
    +                                                 id=\"jupyter-iframe\"
    +                                                 iframe-src={notebookURL}
    +                                                 style=\"position:fixed; top:88px; left:0px; border:none; margin:0; width: 100%; height: calc(100% - 88px); padding-top:0; box-sizing: border-box;\">
    +                                                 你的浏览器不支持 IFrames
    +                                             </DkuFrame>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;