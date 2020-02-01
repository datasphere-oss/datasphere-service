import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div data-extend-template=\"/templates/recipes/single-output-recipe-creation.html\">
    +             <div data-block=\"settings\">
    +                 <div className=\"control-group\">
    +                     <label htmlFor=\"\" className=\"control-label\" />
    +                     <div className=\"controls\">
    +                         <div
    +                             dataset-selector=\"io.inputDataset\"
    +                             available-datasets=\"availableInputDatasets\"
    +                             className=\"qa_recipe_available-datasets-dropdown\"
    +                         />
    +                     </div>
    +                 </div>
    +                 <div className=\"control-group\">
    +                     <label htmlFor=\"\" className=\"control-label\" />
    +                     <div className=\"controls\">
    +                         <div
    +                             dataset-selector=\"io.inputDataset2\"
    +                             available-datasets=\"availableInputDatasets\"
    +                             className=\"qa_recipe_available-datasets-dropdown\"
    +                         />
    +                     </div>
    +                 </div>
    +                 <div style=\"color:grey; font-size:0.9em\">
    +                     <i className=\"icon-info-sign\" style=\"margin-right: 5px;\">
    +                         {' '}
    +                         创建组件后可以添加其他输入.
    +                     </i>
    +                 </div>
    +                 <i className=\"icon-info-sign\" style=\"margin-right: 5px;\" />
    +             </div>
    +             <i className=\"icon-info-sign\" style=\"margin-right: 5px;\">
    +                 <div data-block=\"errors\">
    +                     {activeSchema && !activeSchema.columns.length ? (
    +                         <div className=\"alert alert-error\">第一个数据集没有模式</div>
    +                     ) : null}
    +                     {activeSchema2 && !activeSchema2.columns.length ? (
    +                         <div className=\"alert alert-error\">第二个数据集没有模式</div>
    +                     ) : null}
    +                 </div>
    +             </i>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;