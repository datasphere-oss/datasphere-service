import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div style=\"padding: 10px\">
    +             <form>
    +                 <fieldset>
    +                     <span className=\"fieldLabel\">选择将用于此图表的数据集的哪个部分.</span>
    +                 </fieldset>
    + 
    +                 <fieldset>
    +                     <label className=\"fieldLabel\">
    +                         <input type=\"checkbox\" value={chart.copySelectionFromScript} />使用相同样本作为脚本
    +                     </label>
    +                 </fieldset>
    + 
    +                 {!chart.copySelectionFromScript ? (
    +                     <div>
    +                         {!chart.copySelectionFromScript ? (
    +                             <fieldset>
    +                                 <span className=\"fieldLabel\">Sampling method</span>
    +                                 <select
    +                                     value={chart.refreshableSelection.selection.samplingMethod}
    +                                     ng-options=\"x[0] as x[1] for x in SamplingData.streamSamplingMethods\"
    +                                     options-descriptions=\"SamplingData.streamSamplingMethodsDesc\"
    +                                     layout=\"list\"
    +                                     dku-bs-select=\"\"
    +                                 />
    +                                 <input
    +                                     type=\"checkbox\"
    +                                     value={chart.refreshableSelection.selection.filter.enabled}
    +                                     onChange={() => {
    +                                         chart.refreshableSelection.selection.filter.enabled
    +                                             ? showFilterModal(false)
    +                                             : onChange();
    +                                     }}
    +                                 />
    +                                 过滤记录
    +                                 {chart.refreshableSelection.selection.filter &&
    +                                 chart.refreshableSelection.selection.filter.enabled ? (
    +                                     <span
    +                                         className=\"filter-expression\"
    +                                         onClick={() => {
    +                                             showFilterModal(false);
    +                                         }}
    +                                         toggle=\"tooltip\"
    +                                         style=\"cursor: pointer\"
    +                                         title=\"Click to edit\">
    +                                         <span
    +                                             dangerouslySetInnerHTML={{
    +                                                 __html: 'chart.refreshableSelection.selection.filter|filterNiceRepr'
    +                                             }}
    +                                         />
    +                                     </span>
    +                                 ) : null}
    +                             </fieldset>
    +                         ) : null}
    + 
    +                         <div
    +                             dataset-selection-sampling-details-fields=\"\"
    +                             selection=\"chart.refreshableSelection.selection\"
    +                         />
    +                     </div>
    +                 ) : null}
    +             </form>
    + 
    +             {chart.engineType != 'SQL' && !chart.copySelectionFromScript ? (
    +                 <div className=\"text-right\">
    +                     <button
    +                         onClick={() => {
    +                             save();
    +                         }}
    +                         className=\"btn btn-primary btn-small\">
    +                         保存和刷新样本
    +                     </button>
    +                     <br />
    +                     <small>
    +                         <a
    +                             onClick={() => {
    +                                 saveNoRefresh();
    +                             }}>
    +                             保存 (不强制刷新)
    +                         </a>
    +                     </small>
    +                 </div>
    +             ) : null}
    +             {chart.engineType != 'SQL' && chart.copySelectionFromScript ? (
    +                 <div className=\"text-right\">
    +                     <button
    +                         onClick={() => {
    +                             save();
    +                         }}
    +                         className=\"btn btn-primary btn-small\">
    +                         保存
    +                     </button>
    +                     <br />
    +                     <small>
    +                         为了强制刷新样本,<br /> 在脚本页刷新它
    +                     </small>
    +                 </div>
    +             ) : null}
    +             {chart.engineType == 'SQL' ? (
    +                 <div className=\"text-right\">
    +                     <button
    +                         onClick={() => {
    +                             save();
    +                         }}
    +                         className=\"btn btn-primary btn-small\">
    +                         保存
    +                     </button>
    +                 </div>
    +             ) : null}
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;