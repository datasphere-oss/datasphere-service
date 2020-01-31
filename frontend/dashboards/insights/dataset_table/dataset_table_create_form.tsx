import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"control-group\">
    +             <label className=\"control-label\">源数据集</label>
    +             <div className=\"controls\">
    +                 <div
    +                     object-picker=\"insight.params.datasetSmartName\"
    +                     type=\"DATASET\"
    +                     object=\"hook.sourceObject\"
    +                     error-scope=\"$parent.$parent\"
    +                 />
    +                 <input type=\"hidden\" value={insight.params.datasetSmartName} required={true} />{' '}
    +                 {/* Make the form invalid when no dataset is selected */}
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
    +     );
    + };
    + 
    + export default TestComponent;