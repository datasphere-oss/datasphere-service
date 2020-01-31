import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"control-group\">
    +             <label className=\"control-label\" dku-for=\"#sourceReportInput button\">
    +                 源报表
    +             </label>
    +             <div className=\"controls\">
    +                 <div
    +                     object-picker=\"facade.reportSmartId\"
    +                     type=\"REPORT\"
    +                     object=\"hook.sourceObject\"
    +                     id=\"sourceReportInput\"
    +                     error-scope=\"$parent.$parent\"
    +                 />
    + 
    +                 <input type=\"hidden\" value={facade.reportSmartId} required={true} />
    + 
    +                 <div insight-source-info=\"\" />
    +             </div>
    + 
    +             <div className=\"control-group\">
    +                 <label className=\"control-label\" htmlFor=\"loadLastInput\">
    +                     始终显示上次快照
    +                 </label>
    +                 <div className=\"controls\">
    +                     <input
    +                         type=\"checkbox\"
    +                         value={insight.params.loadLast}
    +                         onChange={() => {
    +                             checkLoadLastAndTimestampConsistency() && insight.params.loadLast && resetTimestamp();
    +                         }}
    +                         id=\"loadLastInput\"
    +                     />
    +                 </div>
    +             </div>
    + 
    +             {canWriteProject() ? (
    +                 <div className=\"control-group\">
    +                     {insight.params.loadLast ? (
    +                         <label className=\"control-label\" htmlFor=\"createSnapshotInput\">
    +                             新的快照
    +                         </label>
    +                     ) : null}
    +                     {!insight.params.loadLast ? (
    +                         <label className=\"control-label\" htmlFor=\"createSnapshotInput\">
    +                             创建并显示新快照
    +                         </label>
    +                     ) : null}
    + 
    +                     <div className=\"controls\">
    +                         <input
    +                             type=\"checkbox\"
    +                             value={facade.createSnapshot}
    +                             onChange={() => {
    +                                 checkLoadLastAndTimestampConsistency() && facade.createSnapshot && resetTimestamp();
    +                             }}
    +                             id=\"createSnapshotInput\"
    +                         />
    +                     </div>
    +                 </div>
    +             ) : null}
    + 
    +             {!insight.params.loadLast && facade.availableSnapshots.length && !facade.createSnapshot ? (
    +                 <div className=\"control-group\">
    +                     <label className=\"control-label\" dku-for=\"#snapshotTimestampInput button\">
    +                         快照显示
    +                     </label>
    +                     <div className=\"controls\" id=\"snapshotTimestampInput\">
    +                         <select
    +                             dku-bs-select=\"\"
    +                             value={facade.snapshot}
    +                             ng-options=\"s as formatDate(s.timestamp) for s in facade.availableSnapshots\"
    +                             onChange={() => {
    +                                 checkLoadLastAndTimestampConsistency();
    +                             }}
    +                         />
    +                     </div>
    +                 </div>
    +             ) : null}
    + 
    +             {!insight.params.loadLast && !facade.availableSnapshots.length && !facade.createSnapshot ? (
    +                 <div className=\"control-group\">
    +                     <div className=\"alert\">无快照可用</div>
    +                 </div>
    +             ) : null}
    + 
    +             {facade.reportSmartId && !facade.availableSnapshots.length && !canWriteProject() ? (
    +                 <div>
    +                     <p>此RMarkdown报表无快照可用, 需要创建一个.</p>
    +                 </div>
    +             ) : null}
    + 
    +             <div className=\"control-group\">
    +                 <label className=\"control-label\" htmlFor=\"insightNameInput\">
    +                     探索名称
    +                 </label>
    +                 <div className=\"controls\">
    +                     <input type=\"text\" value={insight.name} placeholder={hook.defaultName} id=\"insightNameInput\" />
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;