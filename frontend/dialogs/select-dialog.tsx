import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return (
    +         <div className=\"modal modal3\">
    +             <DkuModalHeader modal-title={title} modal-close=\"cancel\" />
    +             <div className=\"modal-body\">
    +                 {text ? <p dangerouslySetInnerHTML={{__html: 'text'}} /> : null}
    +                 <div className=\"selectable-items-list\">
    +                     {items.map((item, index: number) => {
    +                         return (
    +                             <div
    +                                 key={`item-${index}`}
    +                                 className=\"{selected: item === selectedItem}\"
    +                                 onClick={() => {
    +                                     selectItem(item);
    +                                 }}>
    +                                 <div className=\"selection-indicator\">
    +                                     {item === selectedItem ? <i className=\"icon-ok\" title=\"active\" /> : null}
    +                                 </div>
    +                                 <p style=\"margin: 0px 10px 0px 10px\" className=\"ng-binding\">
    +                                     <strong className=\"ng-binding\">
    +                                         {item.title} {item.desc ? '–' : ''}{' '}
    +                                     </strong>
    +                                     {item.desc}
    +                                 </p>
    +                             </div>
    +                         );
    +                     })}
    +                 </div>
    +             </div>
    +             <div
    +                 className=\"modal-footer modal-footer-std-buttons\"
    +                 global-keydown=\"{enter:'confirm();', esc: 'cancel();' }\">
    +                 <button
    +                     className=\"btn btn-lg btn-success\"
    +                     onClick={() => {
    +                         confirm();
    +                     }}>
    +                     OK
    +                 </button>
    +             </div>
    +         </div>
    +     );
    + };
    + 
    + export default TestComponent;