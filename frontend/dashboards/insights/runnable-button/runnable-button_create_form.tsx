import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         <div key=\"child-0\" className=\"control-group\">
    +             <label className=\"control-label\">Runnable</label>
    +             {runnables.length > 0 ? (
    +                 <div className=\"controls\">
    +                     <select
    +                         dku-bs-select=\"\"
    +                         value={selectedRunnable}
    +                         ng-options=\"runnable as (runnable.desc.meta.label || runnable.id) for runnable in runnables\"
    +                         onChange={() => {
    +                             onRunnableSelected(selectedRunnable);
    +                         }}
    +                     />
    +                 </div>
    +             ) : null}
    +             {runnables.length == 0 && runnablesExist ? (
    +                 <div className=\"controls\">
    +                     <span>No runnable is accessible for the current user's rights</span>
    +                 </div>
    +             ) : null}
    +             {runnables.length == 0 && !runnablesExist ? (
    +                 <div className=\"controls\">
    +                     <span>
    +                         No runnable available, add some from the{' '}
    +                         <a href={$state.href('admin.plugins.list')}>plugin list</a>
    +                     </span>
    +                 </div>
    +             ) : null}
    +             <input type=\"hidden\" value={insight.params.runnableType} required={true} />{' '}
    +             {/* Make the form invalid when no runnable is selected */}
    +         </div>,
    + 
    +         <div key=\"child-2\" className=\"control-group\">
    +             <label className=\"control-label\" htmlFor=\"insightNameInput\">
    +                 Insight name
    +             </label>
    +             <div className=\"controls\">
    +                 <input type=\"text\" value={insight.name} placeholder={hook.defaultName} id=\"insightNameInput\" />
    +             </div>
    +         </div>
    +     ];
    + };
    + 
    + export default TestComponent;