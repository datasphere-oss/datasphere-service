import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 new-analysis-modal dku-modal\" ng-controller=\"NewAnalysisModalController\">
    +             <div
    +                 dku-modal-header-with-totem=\"\"
    +                 modal-class=\"noflex\"
    +                 modal-title=\"新建分析\"
    +                 modal-totem=\"icon-dku-nav_analysis\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\" name=\"theform\">
    +                     <div className=\"modal-body\">
    +                         <div block-api-error=\"\" className=\"modal-error\">
    +                             <div className=\"control-group modal-first-cg-clear-totem\">
    +                                 <label htmlFor=\"\" className=\"control-label\">
    +                                     分析数据集
    +                                 </label>
    +                                 <div className=\"controls\">
    +                                     <div
    +                                         dataset-selector=\"newAnalysis.datasetSmartName\"
    +                                         available-datasets=\"availableDatasets\"
    +                                         required={true}
    +                                     />
    +                                 </div>
    +                             </div>
    +                             <div className=\"control-group\">
    +                                 <label htmlFor=\"\" className=\"control-label\">
    +                                     新建分析名称
    +                                 </label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={newAnalysis.name} required={true} />
    +                                 </div>
    +                             </div>
    +                         </div>
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
    +                                 disabled={!newAnalysis.datasetSmartName || theform.$invalid}
    +                                 className=\"btn btn-primary\"
    +                                 onClick={() => {
    +                                     create();
    +                                 }}>
    +                                 创建分析
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