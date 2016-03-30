/*******************************************************************************
 * Copyright (C) 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package com.google.cloud.dataflow.sdk.util.common;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

import java.util.NoSuchElementException;

/**
 * A {@link Reiterator} that supports one-element lookahead during iteration.
 *
 * @param <T> the type of elements returned by this iterator
 */
public final class PeekingReiterator<T> implements Reiterator<T> {
  private T nextElement;
  private boolean nextElementComputed;
  private final Reiterator<T> iterator;

  public PeekingReiterator(Reiterator<T> iterator) {
    this.iterator = checkNotNull(iterator);
  }

  PeekingReiterator(PeekingReiterator<T> it) {
    this.iterator = checkNotNull(checkNotNull(it).iterator.copy());
    this.nextElement = it.nextElement;
    this.nextElementComputed = it.nextElementComputed;
  }

  @Override
  public boolean hasNext() {
    computeNext();
    return nextElementComputed;
  }

  @Override
  public T next() {
    T result = peek();
    nextElementComputed = false;
    return result;
  }

  /**
   * {@inheritDoc}
   *
   * <p>If {@link #peek} is called, {@code remove} is disallowed until
   * {@link #next} has been subsequently called.
   */
  @Override
  public void remove() {
    checkState(!nextElementComputed,
        "After peek(), remove() is disallowed until next() is called");
    iterator.remove();
  }

  @Override
  public PeekingReiterator<T> copy() {
    return new PeekingReiterator<>(this);
  }

  /**
   * Returns the element that would be returned by {@link #next}, without
   * actually consuming the element.
   * @throws NoSuchElementException if there is no next element
   */
  public T peek() {
    computeNext();
    if (!nextElementComputed) {
      throw new NoSuchElementException();
    }
    return nextElement;
  }

  private void computeNext() {
    if (nextElementComputed) {
      return;
    }
    if (!iterator.hasNext()) {
      return;
    }
    nextElement = iterator.next();
    nextElementComputed = true;
  }
}