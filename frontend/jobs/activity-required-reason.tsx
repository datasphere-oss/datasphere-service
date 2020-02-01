import * as React from 'react';
    + 
    + export interface TestComponentProps {
    +     [key: string]: any;
    + }
    + 
    + const TestComponent: React.StatelessComponent<TestComponentProps> = (props) => {
    +     return [
    +         key == 'NEVER_EXECUTED' ? (
    +             <span key=\"child-0\">
    +                 因为他们 <strong>从未被执行过</strong>
    +             </span>
    +         ) : null,
    +         key == 'HAS_REQUIRED_DEPS' ? (
    +             <span key=\"child-2\">
    +                 因为他们 <strong>依赖于其他</strong>
    +             </span>
    +         ) : null,
    +         key == 'NON_RECURSIVE_FORCED_BUILD' ? (
    +             <span key=\"child-4\">
    +                 因为您选择了 <strong>强制构建</strong>
    +             </span>
    +         ) : null,
    +         key == 'FORCED_RECURSIVE_BUILD' ? (
    +             <span key=\"child-6\">
    +                 因为您选择了 <strong>强制构建</strong>
    +             </span>
    +         ) : null,
    +         key == 'TARGET_DATASET_SETTINGS_HAVE_CHANGED' ? (
    +             <span key=\"child-8\">
    +                 因为 <strong>目标数据集的设置</strong> 改变
    +             </span>
    +         ) : null,
    +         key == 'RECIPE_HAS_CHANGED' ? (
    +             <span key=\"child-10\">
    +                 因为 <strong>组件已经改变</strong>
    +             </span>
    +         ) : null,
    +         key == 'SOURCE_DATASET_HAS_CHANGED' ? (
    +             <span key=\"child-12\">
    +                 因为<strong>源数据集</strong> 已经改变
    +             </span>
    +         ) : null,
    +         key == 'SOURCE_DATA_OUT_OF_DATE' ? (
    +             <span key=\"child-14\">
    +                 因为 <strong>源数据</strong> 已经改变
    +             </span>
    +         ) : null
    +     ];
    + };
    + 
    + export default TestComponent;