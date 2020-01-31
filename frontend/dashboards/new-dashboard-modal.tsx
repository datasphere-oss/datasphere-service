import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <div
                 className=\"modal modal3 new-insight-modal dku-modal\"
                 ng-controller=\"NewDashboardModalController\"
                 auto-size=\"false\">
                 <div dku-modal-header=\"\" modal-class=\"noflex has-border\" modal-title=\"创建仪表盘\">
                     <form name=\"newDashboardForm\" className=\"dkuform-modal-horizontal dkuform-modal-wrapper\">
                         <div className=\"modal-body\">
                             <div block-api-error=\"\">
                                 <div className=\"control-group\">
                                     <label className=\"control-label\">仪表盘名称</label>
                                     <div className=\"controls\">
                                         <input
                                             type=\"text\"
                                             value={dashboard.name}
                                             placeholder={`${appConfig.user.displayName}'s dashboard`}
                                             id=\"qa_dashboard_new-dashboard-name-input\"
                                             required={true}
                                         />
                                     </div>
                                 </div>
                             </div>
     
                             <div className=\"modal-footer modal-footer-std-buttons\">
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
                                         create();
                                     }}
                                     id=\"qa_dashboard_new-dashboard-create-button\">
                                     创建仪表盘
                                 </button>
                             </div>
                         </div>
                     </form>
                 </div>
             </div>
         );
     };
     
     export default TestComponent;