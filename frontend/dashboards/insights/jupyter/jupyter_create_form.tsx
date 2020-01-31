import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"control-group\">
    +             <label className=\"control-label\" dku-for=\"#sourceNotebookInput button\">
    +                 源脚本
    +             </label>
    +             <div className=\"controls\">
    +                 <div
    +                     object-picker=\"facade.notebookSmartName\"
    +                     type=\"JUPYTER_NOTEBOOK\"
    +                     object=\"hook.sourceObject\"
    +                     id=\"sourceNotebookInput\"
    +                     error-scope=\"$parent.$parent\"
    +                 />
    + 
    +                 <input type=\"hidden\" value={facade.notebookSmartName} required={true} />
    + 
    +                 <div insight-source-info=\"\" />
    +             </div>
    + 
    +             <div className=\"control-group\">
    +                 <label className=\"control-label\" htmlFor=\"loadLastInput\">
    +                     经常显示最后的导出
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
    +                         <label className=\"control-label\" htmlFor=\"createExportInput\">
    +                             创建新的导出
    +                         </label>
    +                     ) : null}
    +                     {!insight.params.loadLast ? (
    +                         <label className=\"control-label\" htmlFor=\"createExportInput\">
    +                             创建和展示新的导出
    +                         </label>
    +                     ) : null}
    + 
    +                     <div className=\"controls\">
    +                         <input
    +                             type=\"checkbox\"
    +                             value={facade.createExport}
    +                             onChange={() => {
    +                                 checkLoadLastAndTimestampConsistency() && facade.createExport && resetTimestamp();
    +                             }}
    +                             id=\"createExportInput\"
    +                         />
    +                     </div>
    +                 </div>
    +             ) : null}
    + 
    +             {!insight.params.loadLast && facade.availableExports.length && !facade.createExport ? (
    +                 <div className=\"control-group\">
    +                     <label className=\"control-label\" dku-for=\"#exportTimestampInput button\">
    +                         导出到展示
    +                     </label>
    +                     <div className=\"controls\" id=\"exportTimestampInput\">
    +                         <select
    +                             value={insight.params.exportTimestamp}
    +                             ng-options=\"export.timestamp as formatDate(export.timestamp) for export in facade.availableExports\"
    +                             onChange={() => {
    +                                 checkLoadLastAndTimestampConsistency();
    +                             }}
    +                             dku-bs-select=\"\"
    +                             required={true}
    +                         />
    +                     </div>
    +                 </div>
    +             ) : null}
    + 
    +             {facade.notebookSmartName && !facade.availableExports.length && !canWriteProject() ? (
    +                 <div>
    +                     <p>此笔记本没有出口，请要求其所有者创建一个.</p>
    +                 </div>
    +             ) : null}
    + 
    +             <div className=\"control-group\">
    +                 <label className=\"control-label\" htmlFor=\"insightNameInput\">
    +                     洞察名称
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