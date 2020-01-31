import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 dku-modal sql-notebook sql-notebook-history-modal\">
    +             <div dku-modal-header-with-totem=\"\" modal-title=\"Cell history\" modal-totem=\"icon-time\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\" name=\"theform\">
    +                     <div className=\"modal-body\" style=\"padding: 0\">
    +                         {cell.type == 'QUERY' &&
    +                         notebookTmpState.history &&
    +                         (!notebookTmpState.history[cell.id] || !notebookTmpState.history[cell.id].length) ? (
    +                             <div>
    +                                 <h4 style=\"text-align: center; color: #999; padding: 20px; margin-bottom: 0;\">
    +                                     表格历史为空{' '}
    +                                 </h4>
    +                             </div>
    +                         ) : null}
    + 
    +                         <div className=\"main-list selectable-list\">
    +                             {notebookTmpState.history[cell.id].map((hQuery, index: number) => {
    +                                 return (
    +                                     <div key={`item-${index}`} className=\"hQuery\">
    +                                         <div
    +                                             className=\"{selected: cell.$localState.query.id == hQuery.id}\"
    +                                             onClick={() => {
    +                                                 loadQuery(cell, hQuery, true);
    +                                                 dismiss();
    +                                             }}>
    +                                             <div className=\"status\">
    +                                                 {hQuery.state == 'RUNNING' ? (
    +                                                     <i className=\"icon-spinner icon-spin\">
    +                                                         {hQuery.state == 'FAILED' || hQuery.state == 'ABORTED' ? (
    +                                                             <i className=\"text-error icon-remove\">
    +                                                                 {hQuery.state == 'DONE' ? (
    +                                                                     <i className=\"text-success icon-ok\" />
    +                                                                 ) : null}
    +                                                             </i>
    +                                                         ) : null}
    +                                                     </i>
    +                                                 ) : null}
    +                                             </div>
    +                                             {hQuery.state == 'RUNNING' ? (
    +                                                 <i className=\"icon-spinner icon-spin\">
    +                                                     {hQuery.state == 'FAILED' || hQuery.state == 'ABORTED' ? (
    +                                                         <i className=\"text-error icon-remove\">
    +                                                             {hQuery.state == 'DONE' ? (
    +                                                                 <i className=\"text-success icon-ok\">
    +                                                                     <div className=\"sql\">{hQuery.sql.trim()}</div>
    +                                                                     <div className=\"actions\">
    +                                                                         <div className=\"pull-left\">
    +                                                                             <span className=\"date\">
    +                                                                                 {date(hQuery.runOn, 'yyyy/MM/dd HH:mm')}
    +                                                                             </span>
    +                                                                         </div>
    +                                                                         <a
    +                                                                             onClick={() => {
    +                                                                                 createCellWithQuery(
    +                                                                                     hQuery,
    +                                                                                     getSelectedCellIndex() + 1
    +                                                                                 );
    +                                                                             }}
    +                                                                             title=\"在查询中创建一个新的表格\"
    +                                                                             stop-propagation=\"\">
    +                                                                             <i className=\"icon-file-alt\" />
    +                                                                         </a>
    +                                                                         <i className=\"icon-file-alt\">
    +                                                                             <a
    +                                                                                 onClick={() => {
    +                                                                                     removeQuery(hQuery);
    +                                                                                 }}
    +                                                                                 title=\"Remove from history\"
    +                                                                                 stop-propagation=\"\">
    +                                                                                 <i times-icon=\"\" />
    +                                                                             </a>
    +                                                                             <i times-icon=\"\" />
    +                                                                         </i>
    +                                                                     </div>
    +                                                                     <i className=\"icon-file-alt\">
    +                                                                         <i times-icon=\"\" />
    +                                                                     </i>
    +                                                                 </i>
    +                                                             ) : null}
    +                                                         </i>
    +                                                     ) : null}
    +                                                 </i>
    +                                             ) : null}
    +                                         </div>
    +                                         {hQuery.state == 'RUNNING' ? (
    +                                             <i className=\"icon-spinner icon-spin\">
    +                                                 {hQuery.state == 'FAILED' || hQuery.state == 'ABORTED' ? (
    +                                                     <i className=\"text-error icon-remove\">
    +                                                         {hQuery.state == 'DONE' ? (
    +                                                             <i className=\"text-success icon-ok\">
    +                                                                 <i className=\"icon-file-alt\">
    +                                                                     <i times-icon=\"\" />
    +                                                                 </i>
    +                                                             </i>
    +                                                         ) : null}
    +                                                     </i>
    +                                                 ) : null}
    +                                             </i>
    +                                         ) : null}
    +                                     </div>
    +                                 );
    +                             })}
    +                             {hQuery.state == 'RUNNING' ? (
    +                                 <i className=\"icon-spinner icon-spin\">
    +                                     {hQuery.state == 'FAILED' || hQuery.state == 'ABORTED' ? (
    +                                         <i className=\"text-error icon-remove\">
    +                                             {hQuery.state == 'DONE' ? (
    +                                                 <i className=\"text-success icon-ok\">
    +                                                     <i className=\"icon-file-alt\">
    +                                                         <i times-icon=\"\" />
    +                                                     </i>
    +                                                 </i>
    +                                             ) : null}
    +                                         </i>
    +                                     ) : null}
    +                                 </i>
    +                             ) : null}
    +                         </div>
    +                         {hQuery.state == 'RUNNING' ? (
    +                             <i className=\"icon-spinner icon-spin\">
    +                                 {hQuery.state == 'FAILED' || hQuery.state == 'ABORTED' ? (
    +                                     <i className=\"text-error icon-remove\">
    +                                         {hQuery.state == 'DONE' ? (
    +                                             <i className=\"text-success icon-ok\">
    +                                                 <i className=\"icon-file-alt\">
    +                                                     <i times-icon=\"\" />
    +                                                 </i>
    +                                             </i>
    +                                         ) : null}
    +                                     </i>
    +                                 ) : null}
    +                             </i>
    +                         ) : null}
    +                     </div>
    +                     {hQuery.state == 'RUNNING' ? (
    +                         <i className=\"icon-spinner icon-spin\">
    +                             {hQuery.state == 'FAILED' || hQuery.state == 'ABORTED' ? (
    +                                 <i className=\"text-error icon-remove\">
    +                                     {hQuery.state == 'DONE' ? (
    +                                         <i className=\"text-success icon-ok\">
    +                                             <i className=\"icon-file-alt\">
    +                                                 <i times-icon=\"\">
    +                                                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                                                         {notebookTmpState.history[cell.id].length ? (
    +                                                             <button
    +                                                                 type=\"submit\"
    +                                                                 className=\"btn btn-danger\"
    +                                                                 onClick={() => {
    +                                                                     clearHistory(cell.id);
    +                                                                 }}>
    +                                                                 <i className=\"icon-trash\">&nbsp; 清空历史</i>
    +                                                             </button>
    +                                                         ) : null}
    +                                                         <i className=\"icon-trash\">
    +                                                             <button
    +                                                                 type=\"button\"
    +                                                                 className=\"btn btn-default\"
    +                                                                 onClick={() => {
    +                                                                     dismiss();
    +                                                                 }}>
    +                                                                 {' '}
    +                                                                 关闭
    +                                                             </button>
    +                                                         </i>
    +                                                     </div>
    +                                                     <i className=\"icon-trash\" />
    +                                                 </i>
    +                                             </i>
    +                                         </i>
    +                                     ) : null}
    +                                 </i>
    +                             ) : null}
    +                         </i>
    +                     ) : null}
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;