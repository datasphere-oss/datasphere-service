import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <link key=\"child-0\" href=\"../../static/dataiku/css/style.css\" rel=\"stylesheet\" type=\"text/css\" />,
    + 
    +         <div key=\"child-2\" remaining-height=\"\" style={`position:relative;height:${remainingHeight}px`}>
    +             <div fixed-panes=\"\" className=\"fixedPanes showLeftPane\" show-left-pane=\"true\">
    +                 <div className=\"leftPane\">
    +                     <div className=\"header-tabs noflex\">
    +                         <div
    +                             className=\"{'active' : notebookLocalState.leftPaneTab == 'Tables'}\"
    +                             onClick={() => {
    +                                 notebookLocalState.leftPaneTab = 'Tables';
    +                             }}>
    +                             <div className=\"title\">表</div>
    +                         </div>
    +                         <div
    +                             className=\"{'active' : notebookLocalState.leftPaneTab == 'Cells'}\"
    +                             onClick={() => {
    +                                 notebookLocalState.leftPaneTab = 'Cells';
    +                             }}>
    +                             <div className=\"title\">查询</div>
    +                         </div>
    +                     </div>
    +                     {notebookLocalState.leftPaneTab == 'Tables' ? (
    +                         <div className=\"table-explorer\">
    +                             {notebookLocalState ? (
    +                                 <SqlTableExplorer
    +                                     connection=\"notebookParams.connection\"
    +                                     notebook=\"notebookLocalState\"
    +                                     on-table-clicked=\"onTableClicked(table)\"
    +                                     on-field-clicked=\"onFieldClicked(field)\"
    +                                 />
    +                             ) : null}
    +                         </div>
    +                     ) : null}
    + 
    +                     {notebookLocalState && notebookLocalState.leftPaneTab == 'Cells' ? (
    +                         <div include-no-scope=\"/templates/notebooks/sql-notebook-cells-list.html\" />
    +                     ) : null}
    +                 </div>
    + 
    +                 <div className=\"mainPane fh\">
    +                     <div className=\"h100 vertical-flex\">
    +                         <div block-api-error=\"\">
    +                             <div className=\"sql-notebook-toolbar noflex\">
    +                                 <div className=\"tools pull-right\">
    +                                     <span className=\"btn-group\">
    +                                         <button
    +                                             className=\"{active: notebookLocalState.cellMode == 'SINGLE'}\"
    +                                             onClick={() => {
    +                                                 notebookLocalState.cellMode = 'SINGLE';
    +                                             }}
    +                                             title=\"Fullscreen query mode\">
    +                                             <i className=\"icon-resize-full\" />
    +                                         </button>
    +                                         <i className=\"icon-resize-full\">
    +                                             <button
    +                                                 className=\"{active: notebookLocalState.cellMode == 'MULTI'}\"
    +                                                 onClick={() => {
    +                                                     notebookLocalState.cellMode = 'MULTI';
    +                                                 }}
    +                                                 title=\"Stacked queries mode\">
    +                                                 <i className=\"icon-reorder\" />
    +                                             </button>
    +                                             <i className=\"icon-reorder\" />
    +                                         </i>
    +                                     </span>
    +                                     <i className=\"icon-resize-full\">
    +                                         <i className=\"icon-reorder\">
    +                                             <span className=\"btn-group\">
    +                                                 <button
    +                                                     className=\"btn btn-default\"
    +                                                     onClick={() => {
    +                                                         addCell('QUERY');
    +                                                     }}
    +                                                     title=\"添加新的查询\">
    +                                                     <span plus-icon=\"\"> 查询</span>
    +                                                 </button>
    +                                                 <button
    +                                                     className=\"btn btn-default\"
    +                                                     onClick={() => {
    +                                                         addCell('MARKDOWN');
    +                                                     }}
    +                                                     title=\"添加新的 Markdown 单元格\">
    +                                                     <span plus-icon=\"\"> Markdown</span>
    +                                                 </button>
    +                                             </span>
    +                                             {notebookLocalState.cellMode == 'MULTI' ? (
    +                                                 <span className=\"btn-group\">
    +                                                     <button
    +                                                         className=\"btn btn-default\"
    +                                                         onClick={() => {
    +                                                             moveCell(getSelectedCellIndex(), -1);
    +                                                         }}
    +                                                         disabled={!getSelectedCellIndex()}
    +                                                         title=\"往上移动单元格\">
    +                                                         <i className=\"icon-arrow-up\" />
    +                                                     </button>
    +                                                     <i className=\"icon-arrow-up\">
    +                                                         <button
    +                                                             className=\"btn btn-default\"
    +                                                             onClick={() => {
    +                                                                 moveCell(getSelectedCellIndex(), 1);
    +                                                             }}
    +                                                             disabled={
    +                                                                 getSelectedCellIndex() === undefined ||
    +                                                                 getSelectedCellIndex() == cells.length - 1
    +                                                             }
    +                                                             title=\"往下移动单元格\">
    +                                                             <i className=\"icon-arrow-down\" />
    +                                                         </button>
    +                                                         <i className=\"icon-arrow-down\" />
    +                                                     </i>
    +                                                 </span>
    +                                             ) : null}
    +                                             <i className=\"icon-arrow-up\">
    +                                                 <i className=\"icon-arrow-down\">
    +                                                     <span
    +                                                         code-snippet-editor-switch=\"\"
    +                                                         code-samples-selector-visible=\"uiState.codeSamplesSelectorVisible\"
    +                                                     />
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </div>
    +                                 <i className=\"icon-resize-full\">
    +                                     <i className=\"icon-reorder\">
    +                                         <i className=\"icon-arrow-up\">
    +                                             <i className=\"icon-arrow-down\">
    +                                                 {hasUnfoldedCell() && notebookLocalState.cellMode == 'MULTI' ? (
    +                                                     <a
    +                                                         className=\"link-std\"
    +                                                         title=\"Collapse all cells\"
    +                                                         toggle=\"tooltip\"
    +                                                         onClick={() => {
    +                                                             unfoldAllCells(false);
    +                                                         }}>
    +                                                         <i className=\"icon-caret-down\" style=\"margin-right: 3px;\" />
    +                                                     </a>
    +                                                 ) : null}
    +                                                 <i className=\"icon-caret-down\" style=\"margin-right: 3px;\">
    +                                                     {!hasUnfoldedCell() && notebookLocalState.cellMode == 'MULTI' ? (
    +                                                         <a
    +                                                             className=\"link-std\"
    +                                                             title=\"Expand all cells\"
    +                                                             toggle=\"tooltip\"
    +                                                             onClick={() => {
    +                                                                 unfoldAllCells(true);
    +                                                             }}>
    +                                                             <i
    +                                                                 className=\"icon-caret-right\"
    +                                                                 style=\"margin-right: 3px;\"
    +                                                             />
    +                                                         </a>
    +                                                     ) : null}
    +                                                     <i className=\"icon-caret-right\" style=\"margin-right: 3px;\">
    +                                                         {connectionDetails && !connectionFailed ? (
    +                                                             <span
    +                                                                 className=\"title\"
    +                                                                 title={`连接到 ${connectionDetails.label} ${
    +                                                                     connectionDetails.hostname
    +                                                                         ? ' - ' + connectionDetails.hostname
    +                                                                         : ''
    +                                                                 }`}>
    +                                                                 连接到 <strong>{connectionDetails.label}</strong> ({
    +                                                                     connectionDetails.type
    +                                                                 })
    +                                                                 <span custom-element-popup=\"\">
    +                                                                     <a
    +                                                                         className=\"mainzone dropdown-toggle\"
    +                                                                         onClick={() => {
    +                                                                             togglePopover();
    +                                                                         }}>
    +                                                                         <i
    +                                                                             className=\"icon-info-sign\"
    +                                                                             style=\"color:#999;margin-left: 4px;position: relative;top: 1px;\"
    +                                                                         />
    +                                                                     </a>
    +                                                                     <i
    +                                                                         className=\"icon-info-sign\"
    +                                                                         style=\"color:#999;margin-left: 4px;position: relative;top: 1px;\">
    +                                                                         <ul
    +                                                                             className=\"popover custom-element-popup-popover dropdown-menu pull-right simple connection-info-popup\"
    +                                                                             style=\"padding: 10px 20px;\">
    +                                                                             <li>
    +                                                                                 <span className=\"label\">连接名称</span>
    +                                                                                 <span className=\"value\">
    +                                                                                     {connectionDetails.label}
    +                                                                                 </span>
    +                                                                             </li>
    +                                                                             <li>
    +                                                                                 <span className=\"label\">类型</span>
    +                                                                                 <span className=\"value\">
    +                                                                                     {connectionDetails.type}
    +                                                                                 </span>
    +                                                                             </li>
    +                                                                             {connectionDetails.hostname ? (
    +                                                                                 <li>
    +                                                                                     <span className=\"label\">主机</span>
    +                                                                                     <span className=\"value\">
    +                                                                                         {connectionDetails.hostname}
    +                                                                                     </span>
    +                                                                                 </li>
    +                                                                             ) : null}
    +                                                                             <li>
    +                                                                                 <span className=\"label\">数据库名</span>
    +                                                                                 <span className=\"value\">
    +                                                                                     {connectionDetails.database}
    +                                                                                 </span>
    +                                                                             </li>
    +                                                                         </ul>
    +                                                                     </i>
    +                                                                 </span>
    +                                                                 <i
    +                                                                     className=\"icon-info-sign\"
    +                                                                     style=\"color:#999;margin-left: 4px;position: relative;top: 1px;\">
    +                                                                     {/* <span ng-show=\"connectionDetails.hostname\"> &nbsp;-&nbsp;{{connectionDetails.hostname}} </span> */}
    +                                                                 </i>
    +                                                             </span>
    +                                                         ) : null}
    +                                                         <i
    +                                                             className=\"icon-info-sign\"
    +                                                             style=\"color:#999;margin-left: 4px;position: relative;top: 1px;\">
    +                                                             {!connectionDetails || connectionFailed ? (
    +                                                                 <span className=\"title\">
    +                                                                     <i className=\"icon-spin icon-spinner\" />&nbsp;连接...
    +                                                                 </span>
    +                                                             ) : null}
    + 
    +                                                             {connectionFailed ? (
    +                                                                 <span className=\"title\">
    +                                                                     <span className=\"text-error\">
    +                                                                         <i className=\"icon-warning-sign\" />
    +                                                                         <strong>连接失败</strong>
    +                                                                     </span>
    +                                                                 </span>
    +                                                             ) : null}
    +                                                         </i>
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </div>
    +                             <i className=\"icon-resize-full\">
    +                                 <i className=\"icon-reorder\">
    +                                     <i className=\"icon-arrow-up\">
    +                                         <i className=\"icon-arrow-down\">
    +                                             <i className=\"icon-caret-down\" style=\"margin-right: 3px;\">
    +                                                 <i className=\"icon-caret-right\" style=\"margin-right: 3px;\">
    +                                                     <i
    +                                                         className=\"icon-info-sign\"
    +                                                         style=\"color:#999;margin-left: 4px;position: relative;top: 1px;\">
    +                                                         {cells && !cells.length ? (
    +                                                             <div style=\"text-align: center\">
    +                                                                 <h4 style=\"padding-top: 40px;color: #999;\">
    +                                                                     {' '}
    +                                                                     在此脚本中无查询{' '}
    +                                                                 </h4>
    + 
    +                                                                 <div className=\"sql-notebook-footer\">
    +                                                                     <span className=\"btn-group\">
    +                                                                         <button
    +                                                                             className=\"btn btn-default\"
    +                                                                             onClick={() => {
    +                                                                                 addCell('QUERY');
    +                                                                             }}
    +                                                                             title=\"添加一个查询\">
    +                                                                             <span plus-icon=\"\"> 查询</span>
    +                                                                         </button>
    +                                                                     </span>
    +                                                                 </div>
    +                                                             </div>
    +                                                         ) : null}
    + 
    +                                                         {cells.length && notebookLocalState.cellMode == 'MULTI' ? (
    +                                                             <div
    +                                                                 className=\"multi-query-editor selectable-list flex oa\"
    +                                                                 sql-notebook-multi-cell-editor=\"\"
    +                                                                 watch-scroll=\"\">
    +                                                                 {!cell.$tmpState.filteredOut
    +                                                                     ? cells.map((cell, index: number) => {
    +                                                                           return (
    +                                                                               <div
    +                                                                                   key={`item-${index}`}
    +                                                                                   sql-notebook-cell=\"\"
    +                                                                                   ng-switch=\"cell.type\"
    +                                                                                   className=\"{selected: cell.$localState.selected}\"
    +                                                                                   onClick={() => {
    +                                                                                       selectCell($index);
    +                                                                                   }}
    +                                                                                   tabIndex={1}>
    +                                                                                   {!cell.$tmpState.filteredOut ? (
    +                                                                                       <div
    +                                                                                           ng-switch-when=\"QUERY\"
    +                                                                                           sql-notebook-query-cell=\"\"
    +                                                                                           className=\"{running: cell.$tmpState.runningQuery || cell.$tmpState.initializingQuery}\">
    +                                                                                           {!cell.$tmpState
    +                                                                                               .filteredOut ? (
    +                                                                                               <div
    +                                                                                                   ng-switch-when=\"MARKDOWN\"
    +                                                                                                   sql-notebook-md-cell=\"\"
    +                                                                                                   className=\"sql-notebook-md-cell multi\">
    +                                                                                                   {!cell.$tmpState
    +                                                                                                       .filteredOut ? (
    +                                                                                                       <div
    +                                                                                                           ng-switch-default=\"\"
    +                                                                                                           className=\"alert\"
    +                                                                                                           sql-notebook-cell=\"\"
    +                                                                                                           style=\"padding-right: 20px;\">
    +                                                                                                           <div className=\"tools pull-right\">
    +                                                                                                               <a
    +                                                                                                                   onClick={() => {
    +                                                                                                                       removeCell(
    +                                                                                                                           $index
    +                                                                                                                       );
    +                                                                                                                   }}
    +                                                                                                                   className=\"cell-tool link-std\">
    +                                                                                                                   <i className=\"icon-trash\" />
    +                                                                                                               </a>
    +                                                                                                               <i className=\"icon-trash\" />
    +                                                                                                           </div>
    +                                                                                                           <i className=\"icon-trash\">
    +                                                                                                               未知的单元格类型:{' '}
    +                                                                                                               {
    +                                                                                                                   cell.type
    +                                                                                                               }
    +                                                                                                           </i>
    +                                                                                                       </div>
    +                                                                                                   ) : null}
    +                                                                                                   <i className=\"icon-trash\" />
    +                                                                                               </div>
    +                                                                                           ) : null}
    +                                                                                           <i className=\"icon-trash\">
    +                                                                                               <div className=\"sql-notebook-footer\">
    +                                                                                                   <span className=\"btn-group\">
    +                                                                                                       <button
    +                                                                                                           className=\"btn btn-default\"
    +                                                                                                           onClick={() => {
    +                                                                                                               addCell(
    +                                                                                                                   'QUERY'
    +                                                                                                               );
    +                                                                                                           }}
    +                                                                                                           title=\"Add a new Query cell\">
    +                                                                                                           <i className=\"icon-plus\">
    +                                                                                                               {' '}
    +                                                                                                               查询
    +                                                                                                           </i>
    +                                                                                                       </button>
    +                                                                                                       <i className=\"icon-plus\">
    +                                                                                                           <button
    +                                                                                                               className=\"btn btn-default\"
    +                                                                                                               onClick={() => {
    +                                                                                                                   addCell(
    +                                                                                                                       'MARKDOWN'
    +                                                                                                                   );
    +                                                                                                               }}
    +                                                                                                               title=\"Add a new Markdown cell\">
    +                                                                                                               <i className=\"icon-plus\">
    +                                                                                                                   {' '}
    +                                                                                                                   Markdown
    +                                                                                                               </i>
    +                                                                                                           </button>
    +                                                                                                           <i className=\"icon-plus\" />
    +                                                                                                       </i>
    +                                                                                                   </span>
    +                                                                                                   <i className=\"icon-plus\">
    +                                                                                                       <i className=\"icon-plus\" />
    +                                                                                                   </i>
    +                                                                                               </div>
    +                                                                                               <i className=\"icon-plus\">
    +                                                                                                   <i className=\"icon-plus\" />
    +                                                                                               </i>
    +                                                                                           </i>
    +                                                                                       </div>
    +                                                                                   ) : null}
    +                                                                                   <i className=\"icon-trash\">
    +                                                                                       <i className=\"icon-plus\">
    +                                                                                           <i className=\"icon-plus\">
    +                                                                                               {notebookLocalState.cellMode ==
    +                                                                                               'SINGLE' ? (
    +                                                                                                   <div
    +                                                                                                       className=\"single-query-editor flex fh\"
    +                                                                                                       sql-notebook-single-cell-editor=\"\">
    +                                                                                                       {cells.map(
    +                                                                                                           (
    +                                                                                                               cell,
    +                                                                                                               index: number
    +                                                                                                           ) => {
    +                                                                                                               return (
    +                                                                                                                   <div
    +                                                                                                                       key={`item-${index}`}
    +                                                                                                                       sql-notebook-cell=\"\"
    +                                                                                                                       className=\"sql-notebook-cell selected fh\"
    +                                                                                                                       ng-switch=\"cell.type\"
    +                                                                                                                       visible-if=\"cell.$localState.selected\">
    +                                                                                                                       {!cell
    +                                                                                                                           .$tmpState
    +                                                                                                                           .filteredOut ? (
    +                                                                                                                           <div
    +                                                                                                                               ng-switch-when=\"QUERY\"
    +                                                                                                                               sql-notebook-query-single-cell=\"\"
    +                                                                                                                               className=\"{running: cell.$tmpState.runningQuery || cell.$tmpState.initializingQuery}\"
    +                                                                                                                               visible-if=\"cell.$localState.selected\">
    +                                                                                                                               {!cell
    +                                                                                                                                   .$tmpState
    +                                                                                                                                   .filteredOut ? (
    +                                                                                                                                   <div
    +                                                                                                                                       ng-switch-when=\"MARKDOWN\"
    +                                                                                                                                       sql-notebook-md-single-cell=\"\"
    +                                                                                                                                       className=\"sql-notebook-md-cell single fh\"
    +                                                                                                                                       visible-if=\"cell.$localState.selected\">
    +                                                                                                                                       {!cell
    +                                                                                                                                           .$tmpState
    +                                                                                                                                           .filteredOut ? (
    +                                                                                                                                           <div
    +                                                                                                                                               ng-switch-default=\"\"
    +                                                                                                                                               className=\"alert\"
    +                                                                                                                                               sql-notebook-cell=\"\"
    +                                                                                                                                               visible-if=\"cell.$localState.selected\"
    +                                                                                                                                               style=\"padding-right: 20px;\">
    +                                                                                                                                               <div className=\"tools pull-right\">
    +                                                                                                                                                   <a
    +                                                                                                                                                       onClick={() => {
    +                                                                                                                                                           removeCell(
    +                                                                                                                                                               $index
    +                                                                                                                                                           );
    +                                                                                                                                                       }}
    +                                                                                                                                                       className=\"cell-tool link-std\">
    +                                                                                                                                                       <i className=\"icon-trash\" />
    +                                                                                                                                                   </a>
    +                                                                                                                                                   <i className=\"icon-trash\" />
    +                                                                                                                                               </div>
    +                                                                                                                                               <i className=\"icon-trash\">
    +                                                                                                                                                   未知的单元格类型:{' '}
    +                                                                                                                                                   {
    +                                                                                                                                                       cell.type
    +                                                                                                                                                   }
    +                                                                                                                                               </i>
    +                                                                                                                                           </div>
    +                                                                                                                                       ) : null}
    +                                                                                                                                       <i className=\"icon-trash\" />
    +                                                                                                                                   </div>
    +                                                                                                                               ) : null}
    +                                                                                                                               <i className=\"icon-trash\" />
    +                                                                                                                           </div>
    +                                                                                                                       ) : null}
    +                                                                                                                       <i className=\"icon-trash\">
    +                                                                                                                           {uiState.codeSamplesSelectorVisible ? (
    +                                                                                                                               <CodeSnippetSampleSelector
    +                                                                                                                                   insert-code-func=\"insertCodeSnippet\"
    +                                                                                                                                   code-samples-selector-visible=\"uiState.codeSamplesSelectorVisible\"
    +                                                                                                                                   sample-type=\"'sql'\"
    +                                                                                                                                   categories=\"[]\"
    +                                                                                                                                   save-category=\"'na'\"
    +                                                                                                                               />
    +                                                                                                                           ) : null}
    +                                                                                                                       </i>
    +                                                                                                                   </div>
    +                                                                                                               );
    +                                                                                                           }
    +                                                                                                       )}
    +                                                                                                       <i className=\"icon-trash\" />
    +                                                                                                   </div>
    +                                                                                               ) : null}
    +                                                                                               <i className=\"icon-trash\" />
    +                                                                                           </i>
    +                                                                                       </i>
    +                                                                                   </i>
    +                                                                               </div>
    +                                                                           );
    +                                                                       })
    +                                                                     : null}
    +                                                                 <i className=\"icon-trash\">
    +                                                                     <i className=\"icon-plus\">
    +                                                                         <i className=\"icon-plus\">
    +                                                                             <i className=\"icon-trash\" />
    +                                                                         </i>
    +                                                                     </i>
    +                                                                 </i>
    +                                                             </div>
    +                                                         ) : null}
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </div>
    +                     </div>
    +                 </div>
    +             </div>
    +         </div>
    +     ];
    + };
    + 
    + export default TestComponent;