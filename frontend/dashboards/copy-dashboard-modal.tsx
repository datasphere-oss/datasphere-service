import * as React from 'react';
     
 export interface TestComponentProps {
     [key: string]: any;
 }
 
 const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
     return (
         <div
             className=\"modal modal3 move-copy-modal dku-modal\"
             ng-controller=\"CopyDashboardModalController\"
             auto-size=\"false\">
             <div
                 dku-modal-header=\"\"
                 modal-class=\"noflex has-border\"
                 modal-title={withSimpleTiles ? 'Copy' : 'Copy dashboard'}>
                 <form name=\"copyDashboardForm\" className=\"dkuform-modal-horizontal dkuform-modal-wrapper\">
                     <div className=\"modal-body\">
                         <div block-api-error=\"\">
                             <div className=\"control-group\">
                                 <label className=\"control-label\" htmlFor=\"copyDashboardNameInput\">
                                     名称
                                 </label>
                                 <div className=\"controls\">
                                     <input
                                         type=\"text\"
                                         value={dashboard.newName}
                                         placeholder={dashboard.name}
                                         id=\"copyDashboardNameInput\"
                                     />
                                 </div>
                             </div>
 
                             {advancedMode ? (
                                 <div className=\"control-group\">
                                     <div className=\"controls\">
                                         <label>
                                             <input type=\"checkbox\" value={pointerMode} />在新仪表板中创建所有切片，作为指向复制的仪表板的见解的指针，而不是复制它们.
                                         </label>
                                     </div>
                                 </div>
                             ) : null}
                         </div>
 
                         <div className=\"modal-footer modal-footer-std-buttons\">
                             <div className=\"advanced-parameters\">
                                 {' '}
                                 {/* so that it's aligned with the rest of the modal's content */}
                                 <label>
                                     <input type=\"checkbox\" value={advancedMode} />&nbsp;高级参数
                                 </label>
                             </div>
 
                             <button
                                 type=\"button\"
                                 className=\"btn btn-default\"
                                 onClick={() => {
                                     dismiss();
                                 }}>
                                 取消
                             </button>
                             <button
                                 type=\"submit\"
                                 disabled={!copyDashboardForm.$valid}
                                 className=\"btn btn-primary\"
                                 onClick={() => {
                                     copy();
                                 }}>
                                 复制仪表盘
                             </button>
                         </div>
                     </div>
                 </form>
             </div>
         </div>
     );
 };
 
 export default TestComponent;