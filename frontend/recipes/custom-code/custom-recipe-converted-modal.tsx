import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <div dku-modal-header=\"\" modal-title=\"Conversion complete\" modal-class=\"has-border\">
    +                 <form name=\"createForm\" className=\"dkuform-modal-horizontal dkuform-modal-wrapper\">
    +                     <div block-api-error=\"\" />
    +                     <div className=\"modal-body \">
    +                         <div>
    +                             你应该编辑文件 '{data.recipeId}' 在补丁页面上 '{data.pluginId}', 或者直接在{' '}
    +                             {data.pathToFiles}
    +                         </div>
    +                     </div>
    +                     {/* special case : escape key also routes to OK button */}
    +                     <div
    +                         className=\"modal-footer modal-footer-std-buttons\"
    +                         global-keydown=\"{'enter':'resolveModal();', 'esc': 'resolveModal();' }\">
    +                         <button
    +                             type=\"submit\"
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 resolveModal();
    +                             }}>
    +                             返回到我的组件{' '}
    +                         </button>
    +                         <button
    +                             type=\"button\"
    +                             className=\"btn btn-success\"
    +                             onClick={() => {
    +                                 gotoPlugin();
    +                             }}>
    +                             开始编辑
    +                         </button>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;