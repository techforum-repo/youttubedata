/*
 * <license header>
 */

import React from "react";
import {
  Flex,
  ProgressCircle,
} from "@adobe/react-spectrum";

export default function Spinner(props) {
  return (
    <Flex alignItems="center" justifyContent="center" height="50vh">
      <ProgressCircle size="L" aria-label="Loading…" isIndeterminate />
      {props.children}
    </Flex>
  );
}
