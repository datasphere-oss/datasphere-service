import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"scenario-reports h100\" style=\"padding: 20px;\">
    +             <div className=\"h100 vertical-flex\" hierarchical-hover=\"\">
    +                 <div block-api-error=\"\" />
    +                 <div className=\"noflex\">
    +                     <div className=\"above-gantt horizontal-flex\">
    +                         {editable ? (
    +                             <div className=\"range noflex\">
    +                                 <div>
    +                                     <div>
    +                                         <span style=\"line-height: 34px;\">范围</span>
    +                                     </div>
    +                                     <select
    +                                         value={uiState.range}
    +                                         dku-bs-select=\"\"
    +                                         required={true}
    +                                         ng-options=\"range as range|niceConst for range in ranges\"
    +                                     />
    +                                 </div>
    +                             </div>
    +                         ) : null}
    +                         <div className=\"flex\" style=\"position: relative;\">
    +                             {/* don't forget the position:relative otherwise the brush doesn't pick the right width */}
    +                             <div
    +                                 scenario-run-range-brush=\"\"
    +                                 range=\"report.key\"
    +                                 selected-range=\"reportRange\"
    +                                 on-change=\"brushChanged()\"
    +                                 on-drill-down=\"brushDrillDown\"
    +                                 snap-ranges=\"topActions\"
    +                             />
    +                         </div>
    +                     </div>
    +                 </div>
    +                 <div className=\"noflex\">
    +                     <div className=\"above-gantt horizontal-flex\">
    +                         <div className=\"flex\" />
    +                         {/* 'tabular' (formerly 'runs') view is not available on project or instance, because a single scenario is needed */}
    +                         {scenarioId != null ? (
    +                             <div className=\"noflex\">
    +                                 <ul className=\"btn-group dku-btn-group\" style=\"margin-left: 10px;\">
    +                                     <button
    +                                         className=\"{'active' : uiState.viewMode == 'TIMELINE'}\"
    +                                         onClick={() => {
    +                                             uiState.viewMode = 'TIMELINE';
    +                                         }}>
    +                                         <span className=\"title\">Timeline</span>
    +                                     </button>
    +                                     <button
    +                                         className=\"{'active' : uiState.viewMode == 'TABULAR'}\"
    +                                         onClick={() => {
    +                                             uiState.viewMode = 'TABULAR';
    +                                         }}>
    +                                         <span className=\"title\">Tabular</span>
    +                                     </button>
    +                                 </ul>
    +                             </div>
    +                         ) : null}
    +                     </div>
    +                 </div>
    +                 {report.items == null || report.items.length == 0 ? (
    +                     <div className=\"flex oa\">
    +                         <div className=\"fh center-children\">
    +                             <span className=\"range-is-empty\">无场景运行在可选范围</span>
    +                         </div>
    +                     </div>
    +                 ) : null}
    +                 {report.items.length > 0 && uiState.viewMode == 'TIMELINE' ? (
    +                     <div className=\"flex oa graph-zone\">
    +                         <NgInclude src=\"'/templates/scenarios/fragments/gantt.html'\" />
    +                     </div>
    +                 ) : null}
    +                 {report.items.length > 0 && uiState.viewMode == 'TABULAR' ? (
    +                     <div className=\"flex oa graph-zone\">
    +                         <NgInclude src=\"'/templates/scenarios/fragments/tabular.html'\" />
    +                     </div>
    +                 ) : null}
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;