import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return cell.$localState.selected ? (
    +         <div className=\"h100\">
    +             <div className=\"cell-above vertical-flex\">
    +                 <div className=\"cell-header unfolded noflex\">
    +                     <form className=\"dkuform-std-vertical\" style=\"margin:0\">
    +                         <div
    +                             include-no-scope=\"/templates/notebooks/sql-notebook-query-toolsl.html\"
    +                             className=\"pull-right\"
    +                         />
    + 
    +                         <span className=\"cell-title\">
    +                             <input
    +                                 type=\"text\"
    +                                 value={cell.name}
    +                                 placeholder=\"填写此查询名称...\"
    +                                 className=\"in-place-edit\"
    +                                 blur-on-enter=\"\"
    +                                 stop-propagation=\"\"
    +                             />
    +                         </span>
    +                     </form>
    +                 </div>
    + 
    +                 <div local-api-error=\"\" className=\"local-api-error\">
    +                     <div block-api-error=\"\" />
    + 
    +                     <div
    +                         className=\"cell-code flex\"
    +                         ui-keydown=\"{'ctrl-enter meta-enter':'run()', 'ctrl-esc meta-esc':'abort()'}\">
    +                         <textarea
    +                             value={cell.$localState.query.sql}
    +                             ui-codemirror=\"editorOptions()\"
    +                             ui-refresh=\"reflow\"
    +                         />
    +                     </div>
    +                 </div>
    + 
    +                 <div className=\"cell-below vertical-flex\">
    +                     {cell.$tmpState.results.rows.length || cell.$tmpState.runningQuery ? (
    +                         <div className=\"sql-results-header\">
    +                             {(cell.$tmpState.logs && connectionDetails.type == 'Hive') ||
    +                             (cell.$localState.query.sql &&
    +                                 cell.$localState.query.expandedSql &&
    +                                 cell.$localState.query.sql != cell.$localState.query.expandedSql) ? (
    +                                 <span className=\"btn-group\">
    +                                     {cell.$localState.query.sql &&
    +                                     cell.$localState.query.expandedSql &&
    +                                     cell.$localState.query.sql != cell.$localState.query.expandedSql ? (
    +                                         <button
    +                                             className=\"{active: cell.$tmpState.resultsTab == 'QUERY'}\"
    +                                             onClick={() => {
    +                                                 cell.$tmpState.resultsTab = 'QUERY';
    +                                             }}
    +                                             title=\"查询 (带有扩展的变量)\">
    +                                             <i className=\"icon-code\" />
    +                                         </button>
    +                                     ) : null}
    +                                     <i className=\"icon-code\">
    +                                         {cell.$tmpState.logs && connectionDetails.type == 'Hive' ? (
    +                                             <button
    +                                                 className=\"{active: cell.$tmpState.resultsTab == 'LOGS'}\"
    +                                                 onClick={() => {
    +                                                     cell.$tmpState.resultsTab = 'LOGS';
    +                                                 }}
    +                                                 title=\"日志\">
    +                                                 <i className=\"icon-th-list\" />
    +                                             </button>
    +                                         ) : null}
    +                                         <i className=\"icon-th-list\">
    +                                             <button
    +                                                 className=\"{active: !cell.$tmpState.resultsTab || cell.$tmpState.resultsTab == 'RESULTS'}\"
    +                                                 onClick={() => {
    +                                                     cell.$tmpState.resultsTab = 'RESULTS';
    +                                                 }}
    +                                                 title=\"Results\">
    +                                                 <i className=\"icon-table\" />
    +                                             </button>
    +                                             <i className=\"icon-table\" />
    +                                         </i>
    +                                     </i>
    +                                 </span>
    +                             ) : null}
    +                             <i className=\"icon-code\">
    +                                 <i className=\"icon-th-list\">
    +                                     <i className=\"icon-table\">
    +                                         {cell.$tmpState.runningQuery ? (
    +                                             <span>
    +                                                 <i className=\"icon-spin icon-spinner\">
    +                                                     {' '}
    +                                                     开始 {friendlyTimeDelta(cell.$tmpState.runningQuery.runOn)}
    +                                                     {cell.$tmpState.lastQuery.runIn ? (
    +                                                         <span>
    +                                                             (最后一次耗时{' '}
    +                                                             {friendlyDurationSec(cell.$tmpState.lastQuery.runIn / 1000)})
    +                                                         </span>
    +                                                     ) : null}
    +                                                 </i>
    +                                             </span>
    +                                         ) : null}
    +                                         <i className=\"icon-spin icon-spinner\">
    +                                             {!cell.$tmpState.runningQuery && cell.$tmpState.results ? (
    +                                                 <span>
    +                                                     {cell.$tmpState.results.success ? (
    +                                                         <span>
    +                                                             <i className=\"icon-ok-circle\"> 执行</i>
    +                                                         </span>
    +                                                     ) : null}
    +                                                     <i className=\"icon-ok-circle\">
    +                                                         {!cell.$tmpState.results.success ? (
    +                                                             <span>
    +                                                                 <i className=\"icon-remove-circle\"> 失败</i>
    +                                                             </span>
    +                                                         ) : null}
    +                                                         <i className=\"icon-remove-circle\">
    +                                                             时间{' '}
    +                                                             {date(cell.$tmpState.lastQuery.runOn, 'yyyy/MM/dd HH:mm')}
    +                                                             {cell.$tmpState.lastQuery.runIn ? (
    +                                                                 <span>
    +                                                                     (时长{' '}
    +                                                                     {friendlyDurationSec(
    +                                                                         cell.$tmpState.lastQuery.runIn / 1000
    +                                                                     )})
    +                                                                 </span>
    +                                                             ) : null}
    +                                                         </i>
    +                                                     </i>
    +                                                 </span>
    +                                             ) : null}
    +                                             <i className=\"icon-ok-circle\">
    +                                                 <i className=\"icon-remove-circle\">
    +                                                     {cell.$tmpState.results.rows.length ? (
    +                                                         <div className=\"pull-right\">
    +                                                             <span className=\"resultset-count\">
    +                                                                 {cell.$tmpState.results.columns.length} 列. 显示{' '}
    +                                                                 {cell.$tmpState.results.rows.length} of{' '}
    +                                                                 {cell.$tmpState.results.totalRows}
    +                                                                 {cell.$tmpState.results.totalRowsClipped ? (
    +                                                                     <span>+</span>
    +                                                                 ) : null}{' '}
    +                                                                 行
    +                                                             </span>
    +                                                             {!cell.$tmpState.runningQuery ? (
    +                                                                 <span className=\"tools pull-right\">
    +                                                                     {cell.$tmpState.results.totalRowsClipped ? (
    +                                                                         <a
    +                                                                             onClick={() => {
    +                                                                                 computeFullCount();
    +                                                                             }}
    +                                                                             className=\"btn btn-default\">
    +                                                                             <i className=\"icon-refresh\">
    +                                                                                 {' '}
    +                                                                                 &nbsp; 计算完全总数
    +                                                                             </i>
    +                                                                         </a>
    +                                                                     ) : null}
    +                                                                     <i className=\"icon-refresh\">
    +                                                                         <a
    +                                                                             onClick={() => {
    +                                                                                 exportCurrent();
    +                                                                             }}
    +                                                                             className=\"btn btn-success\"
    +                                                                             disabled={
    +                                                                                 !projectSummary.canExportDatasetsData
    +                                                                             }>
    +                                                                             <i className=\"icon-download-alt\">
    +                                                                                 {' '}
    +                                                                                 &nbsp;下载
    +                                                                             </i>
    +                                                                         </a>
    +                                                                         <i className=\"icon-download-alt\" />
    +                                                                     </i>
    +                                                                 </span>
    +                                                             ) : null}
    +                                                             <i className=\"icon-refresh\">
    +                                                                 <i className=\"icon-download-alt\" />
    +                                                             </i>
    +                                                         </div>
    +                                                     ) : null}
    +                                                     <i className=\"icon-refresh\">
    +                                                         <i className=\"icon-download-alt\" />
    +                                                     </i>
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </div>
    +                     ) : null}
    +                     <i className=\"icon-table\">
    +                         <i className=\"icon-spin icon-spinner\">
    +                             <i className=\"icon-ok-circle\">
    +                                 <i className=\"icon-remove-circle\">
    +                                     <i className=\"icon-refresh\">
    +                                         <i className=\"icon-download-alt\">
    +                                             {(cell.$tmpState.results.success &&
    +                                                 cell.$tmpState.results.rows.length &&
    +                                                 !cell.$tmpState.resultsTab) ||
    +                                             cell.$tmpState.resultsTab == 'RESULTS' ? (
    +                                                 <div className=\"cell-results flex\">
    +                                                     <div className=\"running-query-overlay\">
    +                                                         <i className=\"icon-spin icon-spinner\" />
    +                                                     </div>
    +                                                     <i className=\"icon-spin icon-spinner\">
    +                                                         <div
    +                                                             fat-table=\"\"
    +                                                             headers=\"cell.$tmpState.results.columns\"
    +                                                             rows={cell.$tmpState.results.rows}
    +                                                             cell-template=\"/templates/notebooks/sql-notebook-result-cell.html\"
    +                                                             header-template=\"/templates/notebooks/sql-notebook-result-header.html\"
    +                                                             header-height=\"50\"
    +                                                             row-height=\"25\"
    +                                                             as=\"cell\"
    +                                                             digest-child-only=\"true\"
    +                                                             row-index-as=\"rowIndex\"
    +                                                             column-widths=\"columnWidths\"
    +                                                             className=\"sql-results-table\"
    +                                                         />
    +                                                     </i>
    +                                                 </div>
    +                                             ) : null}
    +                                             <i className=\"icon-spin icon-spinner\">
    +                                                 {cell.$tmpState.results.success &&
    +                                                 cell.$tmpState.results.hasResultset &&
    +                                                 !cell.$tmpState.results.rows.length ? (
    +                                                     <div>
    +                                                         <div className=\"alert alert-info\">
    +                                                             <h4>
    +                                                                 <i className=\"icon-info-sign\" />空结果集
    +                                                             </h4>
    +                                                             <p>你的查询成功但是返回一个空结果集.</p>
    +                                                         </div>
    +                                                     </div>
    +                                                 ) : null}
    + 
    +                                                 {cell.$tmpState.results &&
    +                                                 !cell.$tmpState.results.success &&
    +                                                 !cell.$tmpState.runningQuery &&
    +                                                 cell.$localState.query.state == 'ABORTED' ? (
    +                                                     <div>
    +                                                         <div className=\"alert alert-warning\">
    +                                                             <h4>
    +                                                                 <i className=\"icon-warning-sign\" /> 失败
    +                                                             </h4>
    +                                                             <p>{cell.$tmpState.results.errorMessage}</p>
    +                                                         </div>
    +                                                     </div>
    +                                                 ) : null}
    + 
    +                                                 {cell.$tmpState.results &&
    +                                                 !cell.$tmpState.results.success &&
    +                                                 !cell.$tmpState.runningQuery &&
    +                                                 cell.$localState.query.state != 'ABORTED' ? (
    +                                                     <div>
    +                                                         <div className=\"alert alert-error\">
    +                                                             <h4>
    +                                                                 <i className=\"icon-warning-sign\" /> 发生一个错误
    +                                                             </h4>
    +                                                             <p>{cell.$tmpState.results.errorMessage}</p>
    +                                                         </div>
    +                                                     </div>
    +                                                 ) : null}
    + 
    +                                                 {cell.$localState.query.logs && cell.$localState.showLog ? (
    +                                                     <div>
    +                                                         {cell.$localState.foldLogs ? (
    +                                                             <i className=\"icon-caret-right\">
    +                                                                 {cell.$localState.query.logs}
    +                                                             </i>
    +                                                         ) : null}
    +                                                     </div>
    +                                                 ) : null}
    +                                                 {cell.$localState.foldLogs ? (
    +                                                     <i className=\"icon-caret-right\">
    +                                                         {cell.$tmpState.resultsTab == 'LOGS' && cell.$tmpState.logs ? (
    +                                                             <div className=\"flex oa\">
    +                                                                 {cell.$tmpState.logs ? (
    +                                                                     <pre style=\"background: white; border: none;\">
    +                                                                         {cell.$tmpState.logs}
    +                                                                     </pre>
    +                                                                 ) : null}
    +                                                             </div>
    +                                                         ) : null}
    + 
    +                                                         {cell.$tmpState.resultsTab == 'QUERY' ? (
    +                                                             <div className=\"flex oa\">
    +                                                                 {cell.$localState.query.expandedSql ? (
    +                                                                     <pre style=\"background: white; border: none;\">
    +                                                                         {cell.$localState.query.expandedSql}
    +                                                                     </pre>
    +                                                                 ) : null}
    +                                                             </div>
    +                                                         ) : null}
    +                                                     </i>
    +                                                 ) : null}
    +                                             </i>
    +                                         </i>
    +                                     </i>
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-ok-circle\">
    +                     <i className=\"icon-remove-circle\">
    +                         <i className=\"icon-refresh\">
    +                             <i className=\"icon-download-alt\">
    +                                 <i className=\"icon-spin icon-spinner\">
    +                                     {cell.$localState.foldLogs ? (
    +                                         <i className=\"icon-caret-right\">
    +                                             <div
    +                                                 resizer=\"horizontal\"
    +                                                 resizer-height=\"4\"
    +                                                 resizer-top=\".cell-above\"
    +                                                 resizer-bottom=\".cell-below\"
    +                                             />
    +                                         </i>
    +                                     ) : null}
    +                                 </i>
    +                             </i>
    +                         </i>
    +                     </i>
    +                 </i>
    +             </div>
    +         </div>
    +     ) : null;
    + };
    + 
    + export default TestComponent;