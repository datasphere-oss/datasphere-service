import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"control-group\">
    +             <label className=\"control-label\" dku-for=\"#sourceModelInput button\">
    +                 Source model
    +             </label>
    +             <div className=\"controls\">
    +                 <div
    +                     object-picker=\"insight.params.savedModelSmartId\"
    +                     type=\"SAVED_MODEL\"
    +                     object=\"hook.sourceObject\"
    +                     id=\"sourceModelInput\"
    +                     error-scope=\"$parent.$parent\"
    +                 />
    +                 <input type=\"hidden\" value={insight.params.savedModelSmartId} required={true} />{' '}
    +                 {/* Make the form invalid when no dataset is selected */}
    +                 <div insight-source-info=\"\" />
    +                 <div className=\"control-group\">
    +                     <label className=\"control-label\" dku-for=\"#versionInput button\">
    +                         模型版本
    +                     </label>
    +                     <div className=\"controls\" id=\"versionInput\">
    +                         <select value={insight.params.version} dku-bs-select=\"\">
    +                             &gt;
    +                             <option value=\"\" selected={true}>
    +                                 经常显示活跃的版本
    +                             </option>
    +                         </select>
    +                     </div>
    +                 </div>
    +             </div>
    + 
    +             <div className=\"control-group\">
    +                 <label className=\"control-label\" htmlFor=\"insightNameInput\">
    +                     Insight name
    +                 </label>
    +                 <div className=\"controls\">
    +                     <input type=\"text\" value={insight.name} placeholder={hook.defaultName} id=\"insightNameInput\" />
    +                 </div>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;