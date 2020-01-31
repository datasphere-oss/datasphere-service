import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         !transclude ? (
    +             <div
    +                 key=\"child-0\"
    +                 className=\"mainzone dataset-selector-mainzone has-caret\"
    +                 full-click=\"\"
    +                 title={currentlySelected.smartName}>
    +                 <span className=\"caret\">
    +                     {currentlySelected ? (
    +                         <span>
    +                             <a
    +                                 className=\"trigger qa_recipe_selected-dataset\"
    +                                 main-click=\"\"
    +                                 onClick={() => {
    +                                     togglePopover();
    +                                 }}>
    +                                 {currentlySelected.label || currentlySelected.smartName}
    +                             </a>
    +                             <br />
    +                             <small>
    +                                 <span>{currentlySelected.type}</span> -
    +                                 {!currentlySelected.localProject ? (
    +                                     <NavLink to=\"projects.project.foreigndatasets.dataset.explore({datasetFullName: currentlySelected.smartName})\">
    +                                         View
    +                                     </NavLink>
    +                                 ) : null}
    +                                 {currentlySelected.localProject ? (
    +                                     <NavLink to=\"projects.project.datasets.dataset.explore({datasetName: currentlySelected.smartName})\">
    +                                         View
    +                                     </NavLink>
    +                                 ) : null}
    +                             </small>
    +                         </span>
    +                     ) : null}
    + 
    +                     {!currentlySelected ? (
    +                         <a
    +                             main-click=\"\"
    +                             className=\"trigger qa_generic_ds-selector\"
    +                             onClick={() => {
    +                                 togglePopover();
    +                             }}>
    +                             No dataset selected
    +                         </a>
    +                     ) : null}
    +                 </span>
    +             </div>
    +         ) : null,
    + 
    +         transclude ? <div key=\"child-2\" ng-transclude=\"\" className=\"mainzone\" /> : null,
    + 
    +         <div key=\"child-4\" className=\"dataset-selector-popover popover\">
    +             <div className=\"search-wrapper\">
    +                 <input
    +                     type=\"search\"
    +                     autoFocus={true}
    +                     value={filter.query}
    +                     placeholder=\"Filter…\"
    +                     className=\"qa_generic_filter-ds-selector\"
    +                 />
    +             </div>
    +             <div className=\"list-zone\">
    +                 {displayedGroups.map((group, index: number) => {
    +                     return (
    +                         <div key={`item-${index}`} className=\"group\">
    +                             <hr />
    +                             <h4>{group.title}</h4>
    + 
    +                             <ul>
    +                                 {group.datasets.map((object, index: number) => {
    +                                     return (
    +                                         <li
    +                                             key={`item-${index}`}
    +                                             className=\"{disabled: !object.usable, selected: (currentlySelected==object)}\"
    +                                             style=\"min-height: 40px\"
    +                                             full-click=\"\"
    +                                             scroll-to-me={currentlySelected == object}>
    +                                             {object.localProject ? (
    +                                                 <a
    +                                                     href=\"\"
    +                                                     main-click=\"\"
    +                                                     onClick={() => {
    +                                                         itemClicked(object);
    +                                                     }}
    +                                                     className=\"qa_recipe_available-datasets\">
    +                                                     {object.label || object.name}
    +                                                     <small>
    +                                                         {object.datasetType}
    +                                                         <span>{object.usableReason}</span>
    +                                                     </small>
    +                                                 </a>
    +                                             ) : null}
    +                                             {!object.localProject ? (
    +                                                 <a
    +                                                     href=\"\"
    +                                                     main-click=\"\"
    +                                                     onClick={() => {
    +                                                         select(object);
    +                                                     }}>
    +                                                     {object.label || object.name} (from project {object.projectKey})
    +                                                     <small>
    +                                                         {object.datasetType}
    +                                                         <span>{object.usableReason}</span>
    +                                                     </small>
    +                                                 </a>
    +                                             ) : null}
    +                                         </li>
    +                                     );
    +                                 })}
    +                             </ul>
    +                         </div>
    +                     );
    +                 })}
    +             </div>
    +         </div>
    +     ];
    + };
    + 
    + export default TestComponent;