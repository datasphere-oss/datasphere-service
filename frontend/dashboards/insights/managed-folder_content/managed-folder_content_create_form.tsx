import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" className=\"dku-horizontal-selector-list\" style=\"padding-top: 20px\">
    +             <div
    +                 className=\"{selected: !insight.params.singleFile}\"
    +                 onClick={() => {
    +                     insight.params.singleFile = false;
    +                 }}>
    +                 <span className=\"name\">整个文件夹</span>
    +                 <span className=\"description\">浏览和查看整个文件夹</span>
    +             </div>
    +             <div
    +                 className=\"{selected: insight.params.singleFile}\"
    +                 onClick={() => {
    +                     insight.params.singleFile = true;
    +                 }}>
    +                 <span className=\"name\">Single file</span>
    +                 <span className=\"description\">选择一个文件展示</span>
    +             </div>
    +         </div>,
    + 
    +         <div key=\"child-2\" className=\"control-group\">
    +             <label className=\"control-label\">源文件夹</label>
    +             <div className=\"controls\">
    +                 <div object-picker=\"insight.params.folderSmartId\" type=\"MANAGED_FOLDER\" object=\"hook.sourceObject\" />
    +                 <input type=\"hidden\" value={insight.params.folderSmartId} required={true} />{' '}
    +                 {/* Make the form invalid when no folder is selected */}
    +                 <div insight-source-info=\"\" />
    +             </div>
    + 
    +             {insight.params.singleFile ? (
    +                 <div className=\"control-group\">
    +                     <label className=\"control-label\">展示文件</label>
    +                     <div className=\"controls\">
    +                         <select
    +                             dku-bs-select=\"\"
    +                             value={insight.params.filePath}
    +                             ng-options=\"f.path as f.path for f in files\"
    +                         />
    +                         <input type=\"hidden\" value={insight.params.filePath} required={true} />{' '}
    +                         {/* Make the form invalid when no file is selected */}
    +                     </div>
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
    +     ];
    + };
    + 
    + export default TestComponent;