import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"vertical-flex h100\">
    +             <div block-api-error=\"\" />
    +             <div className=\"noflex\">
    +                 <div className=\"horizontal-flex\" style=\"line-height: 45px; padding-right: 10px;\">
    +                     <div style=\"flex: 1 1 1px;  text-align: right;\">
    +                         <label className=\"dku-toggle\" style=\"vertical-align: text-bottom;\">
    +                             <input type=\"checkbox\" value={insight.params.loadLast} />
    +                             <span style=\"margin-right: 5px;\" />
    +                         </label>
    +                         始终显示最后导出
    +                     </div>
    + 
    +                     {insight.params.loadLast ? (
    +                         <div style=\"flex: 1 1 1px; text-align: center;\">
    +                             <span className=\"dibvam\">最后导出 {date(exports[0].timestamp, 'short')}</span>
    +                         </div>
    +                     ) : null}
    + 
    +                     {!insight.params.loadLast ? (
    +                         <div style=\"flex: 1 1 1px; text-align: center\">
    +                             <span style=\"margin-right: 5px;\">Export to display</span>
    +                             <select
    +                                 value={insight.params.exportTimestamp}
    +                                 ng-options=\"export.timestamp as (export.timestamp | date : 'short') for export in exports\"
    +                                 dku-bs-select=\"\"
    +                             />
    +                         </div>
    +                     ) : null}
    + 
    +                     <div style=\"flex: 1 1 1px; text-align: left;\">
    +                         {canWriteProject ? (
    +                             <button
    +                                 className=\"btn btn-success\"
    +                                 onClick={() => {
    +                                     createNewExport();
    +                                 }}>
    +                                 新建导出
    +                             </button>
    +                         ) : null}
    +                     </div>
    +                 </div>
    +             </div>
    + 
    +             <div className=\"flex dku-border-top\">
    +                 {!exportNotFound ? (
    +                     <iframe className=\"fh\" src=\"about:blank\" frameBorder={0} width=\"100%\" height=\"100%\">{`
    +         `}</iframe>
    +                 ) : null}
    +                 {exportNotFound ? (
    +                     <p
    +                         className=\"centered-info\"
    +                         style=\"position:absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);\">
    +                         导出不能发现
    +                         <br />
    +                         <br />
    +                         <small>
    +                             notebook 没有被导出.
    +                             {canWriteProject ? <span>导出它, 用\"创建新导出\"</span> : null}
    +                             {!canWriteProject ? (
    +                                 <span>
    +                                     A data scientist 在它之前能够在仪表盘来查看,必须导出它
    +                                     <br />
    +                                     <doclink page=\"/dashboards/insights/jupyter-notebook\" title=\"更多信息\" />
    +                                 </span>
    +                             ) : null}
    +                         </small>
    +                     </p>
    +                 ) : null}
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;