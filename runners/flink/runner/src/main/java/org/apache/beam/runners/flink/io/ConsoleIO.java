/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.beam.runners.flink.io;

import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PDone;

/**
 * Transform for printing the contents of a {@link org.apache.beam.sdk.values.PCollection}.
 * to standard output.
 *
 * This is Flink-specific and will only work when executed using the
 * {@link org.apache.beam.runners.flink.FlinkPipelineRunner}.
 */
public class ConsoleIO {

  /**
   * A PTransform that writes a PCollection to a standard output.
   */
  public static class Write {

    /**
     * Returns a ConsoleIO.Write PTransform with a default step name.
     */
    public static Bound create() {
      return new Bound();
    }

    /**
     * Returns a ConsoleIO.Write PTransform with the given step name.
     */
    public static Bound named(String name) {
      return new Bound().named(name);
    }

    /**
     * A PTransform that writes a bounded PCollection to standard output.
     */
    public static class Bound extends PTransform<PCollection<?>, PDone> {
      private static final long serialVersionUID = 0;

      Bound() {
        super("ConsoleIO.Write");
      }

      Bound(String name) {
        super(name);
      }

      /**
       * Returns a new ConsoleIO.Write PTransform that's like this one but with the given
       * step
       * name.  Does not modify this object.
       */
      public Bound named(String name) {
        return new Bound(name);
      }

      @Override
      public PDone apply(PCollection<?> input) {
        return PDone.in(input.getPipeline());
      }
    }
  }
}

