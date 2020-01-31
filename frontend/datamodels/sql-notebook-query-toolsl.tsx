import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" className=\"cell-tools\">
    +             {cell.$tmpState.results.success ? (
    +                 <button
    +                     type=\"button\"
    +                     className=\"btn btn-default\"
    +                     onClick={() => {
    +                         createRecipe();
    +                     }}
    +                     disabled={isQueryEmpty() || !cell.$tmpState.results.rows.length}>
    +                     新建组件
    +                 </button>
    +             ) : null}
    + 
    +             {!cell.$tmpState.runningQuery ? (
    +                 <button
    +                     type=\"button\"
    +                     className=\"btn btn-success btn-run-main\"
    +                     onClick={() => {
    +                         run();
    +                     }}
    +                     title=\"Hint: use ctrl+enter to run\">
    +                     <i className=\"icon-play\"> &nbsp; 运行</i>
    +                 </button>
    +             ) : null}
    +             <i className=\"icon-play\">
    +                 {cell.$tmpState.runningQuery ? (
    +                     <button
    +                         type=\"button\"
    +                         className=\"btn btn-danger\"
    +                         onClick={() => {
    +                             abort();
    +                         }}
    +                         title=\"Hint: use ctrl+esc to abort\">
    +                         <i className=\"icon-spinner icon-spin\"> &nbsp; 放弃</i>
    +                     </button>
    +                 ) : null}
    +                 <i className=\"icon-spinner icon-spin\">
    +                     <span
    +                         className=\"cell-tool cell-menu\"
    +                         style=\"display:inline-block\"
    +                         custom-element-popup=\"\"
    +                         cep-position=\"align-right-bottom\"
    +                         close-on-click=\"true\">
    +                         <a
    +                             className=\"mainzone link-std dropdown-toggle\"
    +                             onClick={() => {
    +                                 togglePopover();
    +                             }}>
    +                             <i className=\"icon-cog\" />
    +                         </a>
    +                         <i className=\"icon-cog\">
    +                             <ul
    +                                 className=\"popover custom-element-popup-popover sdropdown-menu pull-right simple\"
    +                                 style=\"padding: 15px\">
    +                                 <form className=\"dkuform-horizontal\">
    +                                     {notebookMode == 'HIVE' ? (
    +                                         <div stop-propagation=\"\" className=\"control-group\">
    +                                             <label className=\"control-label\">Hive 运行时配置</label>
    +                                             <div className=\"controls\">
    +                                                 <select
    +                                                     value={cell.querySettings.inheritConf}
    +                                                     ng-options=\"x for x in  appConfig.hiveExecutionConfigs\">
    +                                                     跨 Hive 配置
    +                                                 </select>
    +                                                 <input type=\"text\" value={it.key} placeholder=\"No key\" />
    +                                             </div>
    +                                             <span>→</span>
    +                                             <div>
    +                                                 <input type=\"text\" value={it.value} placeholder=\"No value\" />
    +                                             </div>
    +                                             <del
    +                                                 onClick={() => {
    +                                                     remove($index);
    +                                                 }}>
    +                                                 ×
    +                                             </del>
    +                                         </div>
    +                                     ) : null}
    +                                 </form>
    +                             </ul>
    +                         </i>
    +                     </span>
    +                 </i>
    +             </i>
    +         </div>,
    +         <i key=\"child-1\" className=\"icon-play\">
    +             <i className=\"icon-spinner icon-spin\">
    +                 <i className=\"icon-cog\">
    +                     {notebookMode == 'HIVE' ? <h2 className=\"settings-section-title\">高级设置 </h2> : null}
    +                     {notebookMode != 'HIVE' ? <h2 className=\"settings-section-title mtop0\">高级设置 </h2> : null}
    +                     <small style=\"display: block; margin-bottom: 20px\">
    +                         你通常不需要修改这些设置. 你可能需要更改它们复杂语句如存储过程
    +                     </small>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">添加 LIMIT 语句</label>
    +                         <div className=\"controls\" stop-propagation=\"\">
    +                             <input
    +                                 type=\"checkbox\"
    +                                 value={cell.querySettings.addLimitToStatement}
    +                                 style=\"margin: -2px 2px 0 2px;\"
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\" stop-propagation=\"\">
    +                         <label className=\"control-label\">执行超出事务</label>
    +                         <div className=\"controls\">
    +                             <input
    +                                 type=\"checkbox\"
    +                                 value={cell.querySettings.statementsOutOfTransaction}
    +                                 style=\"margin: -2px 2px 0 2px;\"
    +                             />
    +                         </div>
    +                     </div>
    + 
    +                     <div className=\"control-group\">
    +                         <label className=\"control-label\">多语句解析</label>
    +                         <div className=\"controls\" stop-propagation=\"\">
    +                             <select
    +                                 value={cell.querySettings.statementsParseMode}
    +                                 ng-options=\"x[0] as x[1] for x in [['SPLIT', 'Parse and split'],['RAW', 'Submit as a single statement']]\">
    +                                 \"提交做为一个单语句\" 可能被需要,对于存储过程或者其他复杂语句 语句执行模式
    +                             </select>
    +                         </div>
    +                     </div>
    + 
    +                     <a
    +                         onClick={() => {
    +                             duplicateCell($index);
    +                         }}
    +                         className=\"cell-tool link-std\"
    +                         title=\"复制查询\">
    +                         <i className=\"icon-copy\" />
    +                     </a>
    +                     <i className=\"icon-copy\">
    +                         <a
    +                             onClick={() => {
    +                                 showHistoryModal();
    +                             }}
    +                             className=\"cell-tool link-std\"
    +                             title=\"显示单元格历史\">
    +                             <i className=\"icon-time\" />
    +                         </a>
    +                         <i className=\"icon-time\">
    +                             <a
    +                                 onClick={() => {
    +                                     removeCell($index);
    +                                 }}
    +                                 className=\"cell-tool link-std\"
    +                                 title=\"删除查询\">
    +                                 <i className=\"icon-trash\" />
    +                             </a>
    +                             <i className=\"icon-trash\" />
    +                         </i>
    +                     </i>
    +                 </i>
    +             </i>
    +         </i>
    +     ];
    + };
    + 
    + export default TestComponent;