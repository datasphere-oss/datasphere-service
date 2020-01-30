import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <div className=\"modal modal3 move-copy-modal dku-modal\" ng-controller=\"CopyPageController\" auto-size=\"false\">
                 <div dku-modal-header=\"\" modal-class=\"noflex has-border\" modal-title=\"拷贝幻灯片\">
                     <form name=\"copyPageForm\" className=\"dkuform-modal-wrapper dkuform-modal-horizontal\">
                         <div className=\"modal-body\">
                             <div block-api-error=\"\">
                                 <div className=\"control-group\">
                                     <label className=\"control-label\" htmlFor=\"copyPageName\">
                                         复制幻灯片名称
                                     </label>
                                     <div className=\"controls\">
                                         <input type=\"text\" value={copyPageName} id=\"copyPageName\" />
                                     </div>
                                 </div>
     
                                 <div className=\"control-group\">
                                     <label className=\"control-label\">到仪表盘</label>
                                     <div className=\"controls\">
                                         <select
                                             dku-bs-select=\"{liveSearch:true}\"
                                             value={targetedDashboard}
                                             ng-options=\"dashboard as dashboard.name for dashboard in dashboards\"
                                         />
                                     </div>
                                 </div>
     
                                 {advancedMode ? (
                                     <div className=\"control-group\">
                                         <div className=\"controls\">
                                             <label>
                                                 <input type=\"checkbox\" value={pointerMode} />
                                                 在新幻灯片中创建所有切片作为指向复制幻灯片的见解的指针，而不是复制它们.
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
                                     className=\"btn btn-primary\"
                                     onClick={() => {
                                         copyPage();
                                     }}>
                                     复制
                                 </button>
                             </div>
                         </div>
                     </form>
                 </div>
             </div>
         );
     };
     
     export default TestComponent;