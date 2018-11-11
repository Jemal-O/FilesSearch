package com.search.visitors.creators;

import com.search.visitors.SavingResultVisitor;

public interface VisitorCreator {

    SavingResultVisitor getVisitor(String... args);
}
