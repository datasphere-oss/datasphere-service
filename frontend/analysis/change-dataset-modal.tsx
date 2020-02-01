import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 new-analysis-modal dku-modal\" ng-controller=\"NewAnalysisOnDatasetModalController\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-class=\"noflex\"
    +                 modal-title=\"更新输入数据集\"
    +                 modal-totem=\"icon-dku-nav_analysis\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\" name=\"theform\">
    +                     <div className=\"modal-body\">
    +                         <div block-api-error=\"\" className=\"modal-error\">
    +                             <div className=\"control-group modal-first-cg-clear-totem\">
    +                                 <label htmlFor=\"\" className=\"control-label\">
    +                                     数据集分析
    +                                 </label>
    +                                 <div className=\"controls\">
    +                                     <div
    +                                         dataset-selector=\"datasetSmartName\"
    +                                         available-datasets=\"availableDatasets\"
    +                                         required={true}
    +                                     />
    +                                 </div>
    +                             </div>
    +                         </div>
    + 
    +                         <div className=\"modal-footer modal-footer-std-buttons\">
    +                             <button
    +                                 type=\"button\"
    +                                 className=\"btn btn-default\"
    +                                 onClick={() => {
    +                                     dismiss();
    +                                 }}>
    +                                 取消
    +                             </button>
    +                             <button
    +                                 type=\"submit\"
    +                                 disabled={!datasetSmartName || theform.$invalid}
    +                                 className=\"btn btn-primary\"
    +                                 onClick={() => {
    +                                     change(datasetSmartName);
    +                                 }}>
    +                                 更改
    +                             </button>
    +                         </div>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;