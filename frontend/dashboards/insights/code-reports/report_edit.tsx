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
    +                         经常显示最后的快照
    +                     </div>
    + 
    +                     <div style=\"text-align: center; width: 600px\" className=\"noflex\">
    +                         {insight.params.loadLast ? (
    +                             <span style=\"margin-right: 30px\">
    +                                 <span className=\"dibvam\">最后的快照 {date(snapshots[0].timestamp, 'short')}</span>
    +                             </span>
    +                         ) : null}
    +                         {!insight.params.loadLast ? (
    +                             <span>
    +                                 <span style=\"margin-right: 5px;\">快照显示</span>
    +                                 <select
    +                                     dku-bs-select=\"\"
    +                                     value={insight.params.exportTimestamp}
    +                                     ng-options=\"s.timestamp as (s.timestamp | date : 'short') for s in snapshots\"
    +                                 />
    +                             </span>
    +                         ) : null}
    + 
    +                         {availablePreviewFormats.length ? (
    +                             <span ng-switch=\"availablePreviewFormats\">
    +                                 <select
    +                                     dku-bs-select=\"{'width':'170px'}\"
    +                                     value={insight.params.viewFormat}
    +                                     ng-options=\"f.name as f.desc for f in availablePreviewFormats\"
    +                                     ng-switch-default=\"\"
    +                                 />
    +                             </span>
    +                         ) : null}
    +                     </div>
    + 
    +                     <div style=\"flex: 1 1 1px; text-align: left;\">
    +                         {canWriteProject ? (
    +                             <button
    +                                 className=\"btn btn-success\"
    +                                 onClick={() => {
    +                                     createSnapshot();
    +                                 }}>
    +                                 新建快照
    +                             </button>
    +                         ) : null}
    +                     </div>
    +                 </div>
    +             </div>
    + 
    +             <div className=\"flex dku-border-top\">
    +                 {!snapshotNotFound ? (
    +                     <iframe
    +                         className=\"fh\"
    +                         sandbox=\"allow-forms allow-pointer-lock allow-popups allow-scripts\"
    +                         src=\"about:blank\"
    +                         frameBorder={0}
    +                         width=\"100%\"
    +                         height=\"100%\">{`
    +         `}</iframe>
    +                 ) : null}
    +                 {snapshotNotFound ? (
    +                     <p
    +                         className=\"centered-info\"
    +                         style=\"position:absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);\">
    +                         快照没有发现
    +                         <br />
    +                         <br />
    +                         <small>
    +                             此报告没有快照.
    +                             {!canWriteProject ? (
    +                                 <span>
    +                                     具有数据科学家配置文件的用户必须先将其快照，然后才能在仪表板上查看.
    +                                     <br />
    +                                     <doclink page=\"/dashboards/insights/report\" title=\"More info\" />
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