import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3 metrics-run-settings\">
    +             <div dku-modal-header=\"\" modal-title=\"Insert a metric value\" modal-class=\"has-border\">
    +                 <form className=\"dkuform-modal-horizontal dkuform-modal-wrapper\">
    +                     <div className=\"modal-body\">
    +                         <fieldset>
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">名称</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={newMetric.name} />
    +                                 </div>
    +                             </div>
    +                         </fieldset>
    +                         <fieldset>
    +                             <div className=\"control-group\">
    +                                 <label className=\"control-label\">值</label>
    +                                 <div className=\"controls\">
    +                                     <input type=\"text\" value={newMetric.value} />
    +                                 </div>
    +                             </div>
    +                         </fieldset>
    +                     </div>
    + 
    +                     <div className=\"modal-footer modal-footer-std-buttons\">
    +                         <button
    +                             className=\"btn btn-default\"
    +                             onClick={() => {
    +                                 dismiss();
    +                             }}>
    +                             取消
    +                         </button>
    +                         <button
    +                             className=\"btn btn-primary\"
    +                             onClick={() => {
    +                                 addMetricPoint(newMetric);
    +                             }}
    +                             disabled={!newMetric.name || !newMetric.value}>
    +                             Add
    +                         </button>
    +                     </div>
    +                 </form>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;