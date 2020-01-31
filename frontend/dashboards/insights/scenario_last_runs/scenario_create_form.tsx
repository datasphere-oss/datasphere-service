import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" className=\"dku-block-selector-list\" style=\"margin-top: 10px;\">
    +             <div
    +                 className=\"{selected: insight.type == 'scenario_last_runs'}\"
    +                 onClick={() => {
    +                     insight.type = 'scenario_last_runs';
    +                 }}>
    +                 <div className=\"image\">
    +                     <img src=\"/static/dataiku/images/dashboards/scenario-timeline.png\" />
    +                 </div>
    +                 <div className=\"name\">最后运行</div>
    +                 <div className=\"description\">显示最后运行作为一个交互的时间线或者一个表</div>
    +             </div>
    +             <div
    +                 className=\"{selected: insight.type == 'scenario_run_button'}\"
    +                 onClick={() => {
    +                     insight.type = 'scenario_run_button';
    +                 }}>
    +                 <div className=\"image\">
    +                     <img src=\"/static/dataiku/images/dashboards/scenario-run-button.png\" />
    +                 </div>
    +                 <div className=\"name\">运行按钮</div>
    +                 <div className=\"description\">显示一个按钮运行一个已选场景</div>
    +             </div>
    +         </div>,
    + 
    +         <div key=\"child-2\" className=\"control-group\">
    +             <label className=\"control-label\">源场景</label>
    +             <div className=\"controls\">
    +                 <div
    +                     object-picker=\"insight.params.scenarioSmartId\"
    +                     type=\"SCENARIO\"
    +                     object=\"hook.sourceObject\"
    +                     error-scope=\"$parent.$parent\"
    +                     permission-mode={insight.type == 'scenario_run_button' ? 'RUN' : 'READ'}
    +                 />
    +                 <input type=\"hidden\" value={insight.params.scenarioSmartId} required={true} />
    + 
    +                 <div insight-source-info=\"\" />
    +             </div>
    + 
    +             <div className=\"control-group\">
    +                 <label className=\"control-label\" htmlFor=\"insightNameInput\">
    +                     洞察名称
    +                 </label>
    +                 <div className=\"controls\">
    +                     <input type=\"text\" value={insight.name} placeholder={hook.defaultName} id=\"insightNameInput\" />
    +                 </div>
    +             </div>
    +         </div>
    +     ];
    + };
    + 
    + export default TestComponent;