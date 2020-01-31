import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 new-project-modal dku-modal\" tab-model=\"modalTabState.active\" auto-size=\"false\">
    +             <div dku-modal-header-with-totem=\"\" modal-title=\"新建工程\" modal-totem=\"icon-plus\">
    +                 <form className=\"dkuform-modal-horizontal\" name=\"newProjectForm\">
    +                     <div className=\"modal-body\">
    +                         <div className=\"control-group\">
    +                             <label htmlFor=\"newProjectName\" className=\"control-label\">
    +                                 名称
    +                             </label>
    +                             <div className=\"controls\">
    +                                 <input
    +                                     type=\"text\"
    +                                     id=\"newProjectName\"
    +                                     placeholder=\"新建工程名\"
    +                                     value={newProject.name}
    +                                     required={true}
    +                                     auto-focus=\"true\"
    +                                 />
    +                             </div>
    +                         </div>
    +                         <div className=\"control-group\">
    +                             <label htmlFor=\"newProjectKey\" className=\"control-label\">
    +                                 工程键
    +                             </label>
    +                             <div className=\"controls\">
    +                                 <input
    +                                     type=\"text\"
    +                                     id=\"newProjectKey\"
    +                                     value={newProject.projectKey}
    +                                     name=\"projectKey\"
    +                                     pattern=\"/^\\w+$/\"
    +                                     placeholder=\"新建工程名\"
    +                                 />
    +                                 <div className=\"help-inline\">项目标识用于引用项目之间的数据集. 创建项目后无法更改.</div>
    +                             </div>
    +                         </div>
    + 
    +                         {!uniq ? (
    +                             <div className=\"alert alert-error\">
    +                                 <i className=\"icon-warning-sign\"> 此项目键已被使用.</i>
    +                             </div>
    +                         ) : null}
    +                         <i className=\"icon-warning-sign\">
    +                             <div block-api-error=\"\" />
    + 
    +                             <div className=\"modal-footer modal-footer-std-buttons\">
    +                                 <button
    +                                     type=\"button\"
    +                                     className=\"btn btn-default\"
    +                                     onClick={() => {
    +                                         dismiss();
    +                                     }}>
    +                                     取消
    +                                 </button>
    +                                 <button
    +                                     type=\"submit\"
    +                                     className=\"btn btn-primary\"
    +                                     onClick={() => {
    +                                         create();
    +                                     }}
    +                                     disabled={newProjectForm.$invalid || !uniq}>
    +                                     创建
    +                                 </button>
    +                             </div>
    +                         </i>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;