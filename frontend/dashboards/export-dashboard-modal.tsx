import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <div
                 className=\"modal modal3 move-export-modal dku-modal\"
                 ng-controller=\"ExportDashboardModalController\"
                 auto-size=\"false\">
                 <div dku-modal-header=\"\" modal-class=\"noflex has-border\" modal-title={modalTitle}>
                     <form
                         name=\"exportDashboardForm\"
                         className=\"dkuform-horizontal dkuform-modal-horizontal dkuform-modal-wrapper\">
                         <div className=\"modal-body\">
                             <div block-api-error=\"\">
                                 {dashboardNotSaved ? (
                                     <div className=\"alert alert-warning\">未保存仪表板，结果可能与当前显示的不同!</div>
                                 ) : null}
                                 <div dashboard-export-form=\"\" params=\"params\" origin=\"modal\" page-idx=\"pageIdx\">
                                     {showCheckbox ? (
                                         <div className=\"control-group\">
                                             <label className=\"control-label\">仅当前页面</label>
                                             <div className=\"controls\">
                                                 <input type=\"checkbox\" value={params.exportOnlyCurrentSlide} />
                                             </div>
                                         </div>
                                     ) : null}
                                 </div>
                                 <div className=\"modal-footer modal-footer-std-buttons dkuform-modal-wrapper\">
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
                                         disabled={exportDashboardForm.$invalid}
                                         className=\"btn btn-primary\"
                                         onClick={() => {
                                             exportDashboard();
                                         }}>
                                         导出仪表盘
                                     </button>
                                 </div>
                             </div>
                         </div>
                     </form>
                 </div>
             </div>
         );
     };
     
     export default TestComponent;