import * as React from 'react';
     
     export interface TestComponentProps {
         [key: string]: any;
     }
     
     const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
         return (
             <div className=\"dashboard-export-dir\">
                 {origin == 'scenario' ? (
                     <div className=\"control-group\">
                         <label className=\"control-label\">仪表盘</label>
                         <div className=\"controls\">
                             <div object-picker=\"params.dashboardId\" type=\"DASHBOARD\" object=\"selectedDashboard.dashboard\" />
                         </div>
                         <div className=\"control-group\">
                             <label className=\"control-label\">文件类型</label>
                             <div className=\"controls\">
                                 <select
                                     dku-bs-select=\"\"
                                     value={params.exportFormat.fileType}
                                     ng-options=\"x for x in fileTypes\">
                                     大小
                                 </select>
                             </div>
                         </div>
                         {params.exportFormat.paperSize != 'CUSTOM' ? (
                             <div className=\"control-group\">
                                 <label className=\"control-label\">目标</label>
                                 <div className=\"controls\">
                                     <select
                                         dku-bs-select=\"\"
                                         value={params.exportFormat.orientation}
                                         ng-options=\"key as value for (key, value) in orientationMap\"
                                     />
                                 </div>
                             </div>
                         ) : null}
                         {params.exportFormat.paperSize == 'CUSTOM' ? (
                             <div className=\"control-group\">
                                 <label className=\"control-label\">宽度</label>
                                 <div className=\"controls\">
                                     <input
                                         type=\"number\"
                                         className=\"input-half-size\"
                                         value={params.exportFormat.width}
                                         name=\"exportWidth\"
                                         ng-min=\"minResW\"
                                         ng-max=\"maxResW\"
                                         step={1}
                                     />
                                     <span className=\"help-inline\">像素</span>
                                     {exportFormController.exportWidth.$invalid ? (
                                         <span className=\"dashboard-export-error dku-error-fade-in\">
                                             选择一个宽度值, 在 {minResW} 和 {maxResW}.
                                         </span>
                                     ) : null}
                                 </div>
                             </div>
                         ) : null}
                         {params.exportFormat.paperSize == 'CUSTOM' ? (
                             <div className=\"control-group\">
                                 <label className=\"control-label\">高度</label>
                                 <div className=\"controls\">
                                     <input
                                         type=\"number\"
                                         className=\"input-half-size\"
                                         value={params.exportFormat.height}
                                         name=\"exportHeight\"
                                         ng-min=\"minResH\"
                                         ng-max=\"maxResH\"
                                         step={1}
                                     />
                                     <span className=\"help-inline\">像素</span>
                                     {exportFormController.exportHeight.$invalid ? (
                                         <span className=\"dashboard-export-error dku-error-fade-in\">
                                             选择一个高度值, 在 {minResH} 和 {maxResH}.
                                         </span>
                                     ) : null}
                                 </div>
                             </div>
                         ) : null}
                     </div>
                 ) : null}
             </div>
         );
     };
     
     export default TestComponent;